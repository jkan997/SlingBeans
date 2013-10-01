/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs;

import java.io.ByteArrayOutputStream;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.jkan997.slingbeans.helper.Base64;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.ServerUrl;
import org.jkan997.slingbeans.helper.ServerUrlParser;
import org.jkan997.slingbeans.helper.StringHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jkan997.slingbeans.entity.WorkflowSet;
import org.jkan997.slingbeans.helper.PropertyType;
import org.jkan997.slingbeans.helper.UrlParamEncoder;
import org.json.ISO8601;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openide.util.actions.SystemAction;

enum FileSystemServer {

    SLING, CRX
}

public class FileSystem extends org.openide.filesystems.FileSystem {

    public final static Charset UTF_8;
    public final static String TEXT_PLAIN;
    public final static FileObject[] EMPTY_FO_ARR = new FileObject[]{};
    private FileSystemServer server;
    private Map<String, Object> changes = new LinkedHashMap<String, Object>();

    static {
        TEXT_PLAIN = "text/plain";
        UTF_8 = Charset.forName("utf-8");
    }
    private String name = null;
    private String password = null;
    private String protocol = "http";
    private String host = "127.0.0.1";
    private int port = 4502;
    private String rootUrl = null;
    private String serverPrefix;
    boolean noLoadMode = false;
    private NodeTypeSet nodeTypes;
    private DefaultHttpClient httpClient;
    private WorkflowSet workflows;

    public FileSystem(String url, String user, String password) {

        ServerUrl serverUrl = ServerUrlParser.parse(url);
        this.protocol = serverUrl.protocol;
        this.host = serverUrl.host;
        this.port = serverUrl.port;
        this.name = user;
        this.password = password;
        init();
    }

    public FileSystem(String protocol, String host, int port, String user, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.name = user;
        this.password = password;
        init();

    }

    public void init() {
        serverPrefix = String.format("%s://%s:%d", protocol, host, port);
        String crxRootUrl = serverPrefix + "/crx/server/crx.default/jcr:root";
        String slingRootUrl = serverPrefix + "/server/default/jcr:root";
        byte[] data = getData(slingRootUrl, ".0.json");
        if (data != null) {
            server = FileSystemServer.SLING;
            rootUrl = slingRootUrl;
        }
        data = getData(crxRootUrl, ".0.json");
        if (data != null) {
            server = FileSystemServer.CRX;
            rootUrl = crxRootUrl;
        }
        if (server == null) {
            throw new RuntimeException("Unable to connect server.");
        }
    }

    public NodeTypeSet readTypes() {
        FileObject nodeTypesFo = this.getFileObject("jcr:system/jcr:nodeTypes", 2);
        nodeTypes = new NodeTypeSet();
        nodeTypes.init(nodeTypesFo);
        return nodeTypes;
    }

    public WorkflowSet readWorkflows() {
        FileObject workflowRootFo = this.getFileObject("etc/workflow/models", 10);
        workflows = new WorkflowSet();
        workflows.init(workflowRootFo);
        return workflows;

    }

    public NodeTypeSet getNodeTypes() {
        return nodeTypes;
    }

    public FileSystem() {
        this("http", "127.0.0.1", 4502, "admin", "admin");
    }

    @Override
    public String getDisplayName() {
        return "SlingFS";
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public FileObject getRoot() {
        return getFileObject("");
    }

    @Override
    public FileObject findResource(String path) {
        return getFileObject(path);
    }
    private Map<String, FileObject> cache = Collections.synchronizedMap(new HashMap<String, FileObject>());

    private synchronized FileObject getCachedObject(String path) {
        return getCachedObject(path, true);
    }

    private synchronized FileObject getCachedObject(String path, boolean createNew) {
        path = StringHelper.normalizePath(path);
        FileObject res = null;
        if (cache.containsKey(path)) {
            res = cache.get(path);
        } else {
            if (createNew) {
                res = new FileObject(this);
                res.setPath(path);
                cache.put(path, res);
            }
        }
        return res;
    }

    private FileObject loadFileObjectContent(String path, JSONObject jobj) throws Exception {
        path = StringHelper.normalizePath(path);
        FileObject res = getCachedObject(path);
        String[] nameExtArr = StringHelper.extractNameExt(path);
        res.setName(nameExtArr[0]);
        res.setExt(nameExtArr[1]);
        Object typeObj = jobj.get("jcr:primaryType");
        res.setFolder(true);
        if ((typeObj != null) && ("nt:file".equals(typeObj))) {
            res.setFolder(false);
        }
        res.objectModified();
        return res;
    }

    private FileObject loadFileObject(String path, JSONObject json) throws Exception {
        FileObject res = null;

        path = StringHelper.normalizePath(path);
        res = loadFileObjectContent(path, json);

        JSONObject childJson = null;
        String childPath = null;
        List<FileObject> childrenList = new ArrayList<FileObject>();
        FileObject childFo = null;
        String childPrimaryType = null;
        int childCount = 0;
        for (String key : json.keySet()) {
            childJson = json.getJSONObject(key);
            // System.out.println(key + "=" + childJson);
            if (childJson != null) {
                childPath = (path.length() > 0 ? path + "/" : "") + key;
                childPrimaryType = childJson.getString("jcr:primaryType");
                if (childPrimaryType == null) {
                    childrenList = null;
                    break;
                }
                if (childPrimaryType != null) {
                    childCount++;
                    childFo = loadFileObject(childPath, childJson);
                    childrenList.add(childFo);
                }
            }
        }

        Map<String, FileObjectAttribute> attrsMap = res.getAttributesMap();
        loadFileObjectAttributes(attrsMap, json);
        res.setChildren(childrenList);
        return res;
    }

    public void loadFileObjectAttributes(Map<String, FileObjectAttribute> attrsMap, JSONObject json) {
        attrsMap.clear();

        for (Map.Entry<String, Object> me : json.entrySet()) {
            String key = me.getKey();
            Object obj = me.getValue();
            String type = null;
            String typeKey = null;
            if (!(obj instanceof JSONObject)) {
                FileObjectAttribute foa = new FileObjectAttribute();
                foa.setJsonValue(json, key);
                attrsMap.put(key, foa);
            }
        }
    }

    public FileObject getFileObject(FileObject res) {
        return this.getFileObject(res, false);
    }

    public FileObject getFileObject(String path) {
        return this.getFileObject(path, false);
    }

    public FileObject getFileObject(FileObject res, boolean forceRefresh) {
        return getFileObject(res.getPath(), forceRefresh);
    }

    public FileObject getFileObject(String path, boolean forceRefresh) {
        LogHelper.logInfo(this, "Get file object %s", path);
        path = StringHelper.normalizePath(path);
        if (!forceRefresh) {
            FileObject res = getCachedObject(path, false);
            if (res != null) {
                return res;
            }
        }
        return getFileObject(path, 1);
    }

    public FileObject getFileObject(String path, int childDepth) {
        FileObject res = null;
        path = StringHelper.normalizePath(path);
        try {
            final String jcrPath = String.format("%s.%s.json", path, childDepth);
            byte[] jsonBytes = getData(jcrPath);
            String jsonStr = new String(jsonBytes);
            JSONTokener jsonTokener = new JSONTokener(jsonStr);
            JSONObject json = new JSONObject(jsonTokener);
            res = loadFileObject(path, json);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        if (res == null) {
            LogHelper.logInfo(this, "Null file returned for path %s", path);
        }
        return res;
    }

    byte[] getFileContent(String path) {
        path = StringHelper.normalizePath(path);
        byte[] res = null;
        try {
            final String jcrPath = path + "/jcr:content/jcr:data";
            res = getData(jcrPath);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return res;
    }

    public void sendPost(Map<String, Object> changes) {
        String url = rootUrl;
        sendPost(url, changes, null);
    }

    private boolean isShortVal(String s) {
        return ((s.length() < 10000) && (s.indexOf('\n') == -1));
    }

    private synchronized DefaultHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;

    }

    public byte[] sendSimplePost(String url, Map<String, String> params) {
        if ((!url.startsWith("http://")) && (!url.startsWith("https://"))) {
            url = serverPrefix + (!url.startsWith("/") ? "/" : "") + url;
        }
        try {
            httpClient = getHttpClient();
            HttpPost post = new HttpPost(url);
            configureAuth(post);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> me : params.entrySet()) {
                nvps.add(new BasicNameValuePair(me.getKey(), me.getValue()));
            }
            UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
            post.setEntity(reqEntity);
            long timeStamp = System.currentTimeMillis();
            dumpHttpRequest(reqEntity, timeStamp);
            HttpResponse response = httpClient.execute(post);
            HttpEntity resEntity = response.getEntity();
            byte[] res = getResponseBytes(resEntity, timeStamp);
            return res;
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return null;
    }

    public byte[] sendGet(String url, Map<String, String> params) {
        if ((!url.startsWith("http://")) && (!url.startsWith("https://"))) {
            url = serverPrefix + (!url.startsWith("/") ? "/" : "") + url;
        }
        try {
            httpClient = getHttpClient();
            StringBuilder urlSb = new StringBuilder();
            urlSb.append(url + "?");
            boolean first = true;
            for (Map.Entry<String, String> me : params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    urlSb.append("&");
                }
                urlSb.append(me.getKey());
                urlSb.append("=");
                urlSb.append(URLEncoder.encode(me.getValue()));
            }
            System.out.println(urlSb);
            long timeStamp = System.currentTimeMillis();
            dumpHttpRequest(urlSb, timeStamp);
            HttpGet get = new HttpGet(urlSb.toString());
            configureAuth(get);
            HttpResponse response = httpClient.execute(get);
            HttpEntity resEntity = response.getEntity();
            byte[] res = getResponseBytes(resEntity, timeStamp);
            get.releaseConnection();
            return res;
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return null;
    }

    public byte[] sendPost(String url, Map<String, Object> changes, Map<String, Object> otherParams) {
        if ((!url.startsWith("http://")) && (!url.startsWith("https://"))) {
            url = serverPrefix + (!url.startsWith("/") ? "/" : "") + url;
        }
        if (((changes == null) || (changes.isEmpty())) && ((otherParams == null) || (otherParams.isEmpty()))) {
            return null;
        }
        try {
            httpClient = getHttpClient();
            LogHelper.logInfo(this, url);
            StringBuilder diff = new StringBuilder();
            HttpPost post = new HttpPost(url);
            configureAuth(post);
            MultipartEntity reqEntity = new MultipartEntity(null, "slingfs", UTF_8);
            Set<String> skipVals = new TreeSet<String>();
            if ((changes != null) && (!changes.isEmpty())) {
                boolean first = true;
                for (Map.Entry<String, Object> me : changes.entrySet()) {
                    String val = me.getValue().toString();
                    String key = me.getKey();
                    if (first) {
                        first = false;
                    } else {
                        diff.append("\r\n");
                    }
                    if (key.startsWith("-")) {
                        skipVals.add(key);
                        diff.append(key + " : " + val);
                    } else if (key.startsWith("+")) {
                        diff.append(key + " : " + val);
                        skipVals.add(key);
                    } else {
                        diff.append("^/" + key + " : ");
                    }
                }
                reqEntity.addPart(":diff", new StringBody(diff.toString(), TEXT_PLAIN, UTF_8));
                for (Map.Entry<String, Object> me : changes.entrySet()) {
                    String key = me.getKey();
                    if (!skipVals.contains(key)) {
                        if ((key.startsWith("^/")) || (key.startsWith("+/"))) {
                            key = key.substring(2);
                        }
                        String jcrPath = "/" + key;
                        reqEntity.addPart(jcrPath, generateBody(me.getKey(), me.getValue()));
                    }
                }
            }
            if ((otherParams != null) && (!otherParams.isEmpty())) {
                for (Map.Entry<String, Object> me : otherParams.entrySet()) {
                    reqEntity.addPart(me.getKey(), generateBody(me.getKey(), me.getValue()));
                }
            }
            post.setEntity(reqEntity);
            long timeStamp = System.currentTimeMillis();
            dumpHttpRequest(reqEntity, timeStamp);
            HttpResponse response = httpClient.execute(post);
            HttpEntity resEntity = response.getEntity();
            byte[] res = getResponseBytes(resEntity, timeStamp);
            post.releaseConnection();
            return res;
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return null;
    }

    public void setFileContent(String path, byte[] content, boolean create) {
        FileSystem.setFileContent(path, content, create, changes);
    }

    public static void setFileContent(String path, byte[] content, boolean create, Map<String, Object> changes) {
        path = StringHelper.normalizePath(path);

        String contentStr = (content == null ? "" : new String(content));
        if (!create) {
            changes.put(path + "/jcr:content/jcr:data", contentStr);
        } else {
            changes.put("+/" + path, "{\"jcr:primaryType\":\"nt:file\"}");
            changes.put("+/" + path + "/jcr:content", "{\"jcr:primaryType\":\"nt:resource\"}");
            changes.put(path + "/jcr:content/jcr:data", contentStr);
            changes.put(path + "/jcr:content/jcr:mimeType", "application/octet-stream");
            changes.put(path + "/jcr:content/jcr:encoding", "utf-8");
        }
        changes.put(path + "/jcr:content/jcr:lastModified", new Date());
    }

    public void setFileContent(String path, byte[] content) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new HashMap<String, Object>();
            setFileContent(path, content, false, changes);
            sendPost(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    public void createNode(String path, String nodeType) {
        FileSystem.createNode(path, nodeType, changes);
    }

    public static void createNode(String path, String nodeType, Map<String, Object> changes) {
        path = StringHelper.normalizePath(path);
        String folderJson = String.format("{\"jcr:primaryType\":\"%s\"}", nodeType);
        changes.put("+/" + path, folderJson);
    }

    public static void setNodeAttribute(String nodePath, String attrName, Object value, Map<String, Object> changes) {
        String key = nodePath + "/" + attrName;
        changes.put(key, value);
    }

    public static void createFile(String path, String content, Map<String, Object> changes) {
        if (content == null) {
            content = "";
        }
        createNode(path, "nt:file", changes);
        String contentPath = path + "/jcr:content";
        createNode(contentPath, "nt:resource", changes);
        setNodeAttribute(contentPath, "jcr:data", content, changes);
    }

    public static void createFolder(String path, Map<String, Object> changes) {
        createNode(path, NodeTypeSet.SLING_FOLDER, changes);
    }

    public void createFile(String path, String content) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new LinkedHashMap<String, Object>();
            createFile(path, "", changes);
            sendPost(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    public void createFolder(String path) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new LinkedHashMap<String, Object>();
            createFolder(path, changes);
            sendPost(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    public static void remove(String path, Map<String, Object> changes) {
        path = StringHelper.normalizePath(path);
        changes.put("-/" + path, "");
    }

    public void remove(String path) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new HashMap<String, Object>();
            remove(path, changes);
            sendPost(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    private void configureAuth(AbstractHttpMessage method) {
        String authString = name + ":" + password;
        LogHelper.logInfo(this, "auth string: " + authString);
        String authStringEnc = Base64.encodeBytes(authString.getBytes());
        LogHelper.logInfo(this, "Base64 encoded auth string: " + authStringEnc);
        method.addHeader("Authorization", "Basic " + authStringEnc);
    }

    private byte[] getData(String urlPart) {
        return getData(rootUrl, urlPart);
    }

    private byte[] getData(String serverPart, String urlPart) {
        byte[] res = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            String[] urlArr = urlPart.split("/");
            StringBuilder urlSb = new StringBuilder();
            for (String s : urlArr) {
                if (urlSb.length() > 0) {
                    urlSb.append("/");
                }
                urlSb.append(UrlParamEncoder.encode(s));
            }

            String url = String.format("%s/%s", serverPart, urlSb.toString());
            //LogHelper.logInfo(this, url);
            HttpGet httpget = new HttpGet(url);
            configureAuth(httpget);
            LogHelper.logInfo(this, "Executing request %s", httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            int code = response.getStatusLine().getStatusCode();
            LogHelper.logInfo(this, "Status code %d", code);
            if (code == 200) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                res = IOHelper.readInputStreamToBytes(is);
                is.close();
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return res;
    }

    public void commmit() {
        this.sendPost(changes);
        changes.clear();
    }

    public void rollback() {
        changes.clear();
    }

    public boolean isNoLoadMode() {
        return noLoadMode;
    }

    public void setNoLoadMode(boolean noLoadMode) {
        this.noLoadMode = noLoadMode;
    }

    @Override
    public SystemAction[] getActions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ContentBody generateBody(String key, Object value) throws Exception {
        String mimeType = TEXT_PLAIN;
        String contentStr = null;
        String valStr = value.toString();
        if (value instanceof Date) {
            mimeType = "jcr-value/" + PropertyType.TYPENAME_DATE.toLowerCase();
            valStr = ISO8601.format((Date) value);
        }
        if (value instanceof Long) {
            mimeType = "jcr-value/" + PropertyType.TYPENAME_LONG.toLowerCase();
            valStr = value.toString();
        }
        if (value instanceof Double) {
            mimeType = "jcr-value/" + PropertyType.TYPENAME_DOUBLE.toLowerCase();
            valStr = value.toString().replace(',', '.');
        }
        if (value instanceof Boolean) {
            mimeType = "jcr-value/" + PropertyType.TYPENAME_BOOLEAN.toLowerCase();
            valStr = value.toString().toLowerCase();
        }
        if (key.endsWith("jcr:data")) {
            mimeType = "jcr-value/binary";
        }
        StringBody res = new StringBody(valStr, mimeType, UTF_8);
        return res;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getFileSystemId() {
        return generateFileSystemId(this.protocol, this.host, this.port);
    }

    public static String generateFileSystemId(String protocol, String host, int port) {
        String res = String.format("/%s/%s/%s/%d", "sling", protocol, host, port);

        return res;
    }

    public static String generateFileSystemId(String url) {
        ServerUrl serverUrl = ServerUrlParser.parse(url);
        return generateFileSystemId(serverUrl.protocol, serverUrl.host, serverUrl.port);
    }

    @Override
    public String toString() {
        return this.getFileSystemId();
    }

    private void dumpHttpRequest(Object obj, long timeStamp) throws Exception {
        HttpEntity reqEntity = null;
        if (obj instanceof HttpEntity) {
            reqEntity = (HttpEntity) obj;
        }
        String method = reqEntity != null ? "post" : "get";
        FileOutputStream fos = new FileOutputStream(LogHelper.HTTP_PREFIX + "/" + timeStamp + "." + method + ".log");
        if (reqEntity != null) {
            reqEntity.writeTo(fos);
        } else {
            fos.write(obj.toString().getBytes());
        }
        fos.close();
    }

    private byte[] getResponseBytes(HttpEntity resEntity, long timeStamp) throws IOException {
        FileOutputStream fos = new FileOutputStream(LogHelper.HTTP_PREFIX + "/" + timeStamp + ".response.log");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOHelper.readInputStreamToOutputStream(resEntity.getContent(), bos);
        fos.write(bos.toByteArray());
        fos.close();
        return bos.toByteArray();
    }

    public String getServerPrefix() {
        return serverPrefix;
    }

    public List<String> executeQuery(String query) {
        List<String> res = new ArrayList<String>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("_charset_", "utf-8");
        params.put("_dc", "" + System.currentTimeMillis());
        params.put("showResults", "true");
        params.put("type", "JCR-SQL2");
        params.put("stmt", query);
        byte[] data = sendGet("/crx/de/query.jsp", params);
        if (data != null) {
            String jsonStr = new String(data);
            JSONTokener jsonTokener = new JSONTokener(jsonStr);
            JSONObject json = new JSONObject(jsonTokener);
            JSONArray jsonArr = json.getJSONArray("results");
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject result = jsonArr.getJSONObject(i);
                String path = result.getString("path");
                res.add(path);
            }
        }
        return res;
    }

    public boolean isCQ5() {
        return this.server == FileSystemServer.CRX;
    }
}
