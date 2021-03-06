/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.slingfs;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.jkan997.slingbeans.helper.Base64;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.ServerUrl;
import org.jkan997.slingbeans.helper.ServerUrlParser;
import org.jkan997.slingbeans.helper.StringHelper;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jkan997.slingbeans.entity.WorkflowSet;
import org.jkan997.slingbeans.helper.Disposable;
import org.jkan997.slingbeans.helper.HttpClientHelper;
import org.jkan997.slingbeans.helper.MimeTypeHelper;
import org.jkan997.slingbeans.helper.ObjectHelper;
import org.jkan997.slingbeans.helper.PropertyType;
import org.jkan997.slingbeans.helper.UrlParamEncoder;
import org.jkan997.slingbeans.slingfs.types.NodeType;
import org.jkan997.slingbeans.vlt.VltManager;
import org.json.ISO8601;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openide.util.actions.SystemAction;

enum FileSystemServer {

    SLING, CRX
}

public class FileSystem extends org.openide.filesystems.FileSystem implements Disposable {

    public final static Charset UTF_8;
    public final static String TEXT_PLAIN;
    public final static FileObject[] EMPTY_FO_ARR = new FileObject[]{};
    private FileSystemServer server;
    private Map<String, Object> changes = new LinkedHashMap<String, Object>();
    private Set<Disposable> relatedObjects = new HashSet<Disposable>();

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
    private UsernamePasswordCredentials credentials;
    private VltManager vltManager;

    private String clipboard;
    private boolean clipboardCopy;

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

    protected final void init() {
        httpClient = HttpClientHelper.createHttpClient(name, password);
        credentials = new UsernamePasswordCredentials(name, password);
        LogHelper.logInfo(this, "Credentials %s [%s]", name, password);
        serverPrefix = String.format("%s://%s:%d", protocol, host, port);
        String crxRepository = serverPrefix + "/crx/server/crx.default";
        String crxRootUrl = crxRepository + "/jcr:root";
        String slingRepository = serverPrefix + "/server/default";
        String slingRootUrl = slingRepository + "/jcr:root";
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
        vltManager = new VltManager((server == FileSystemServer.SLING ? slingRepository : crxRepository), name, password);
    }

    public NodeTypeSet readTypes() {
        FileObject nodeTypesFo = this.getFileObject("jcr:system/jcr:nodeTypes", 2);
        nodeTypes = new NodeTypeSet();
        nodeTypes.init(nodeTypesFo);
        return nodeTypes;
    }

    public VltManager getVltManager() {
        return vltManager;
    }

    public WorkflowSet readWorkflows() {
        workflows = new WorkflowSet();
        workflows.init(this);
        
        
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
        } else if (createNew) {
            res = new FileObject(this);
            res.setPath(path);
            cache.put(path, res);
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
            boolean sortChildren = false;
            if (res.getParent() == null) {
                sortChildren = true;
            } else if (this.nodeTypes != null) {
                NodeType nodeType = this.nodeTypes.getByName(res.getPrimaryType());
                if ((nodeType != null) && (nodeType.isHasOrderableChildNodes())) {
                    sortChildren = true;
                }
            }
            //sortChildren = false;
            if (sortChildren) {
                Collections.sort(childrenList);
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

    private DefaultHttpClient getHttpClient() {
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
            post.releaseConnection();
            return res;
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return null;
    }
    
    public byte[] sendGet(String url) {
        return sendGet(url, Collections.EMPTY_MAP, true);
    }

    public byte[] sendGet(String url, Map<String, String> params) {
        return sendGet(url, params, true);
    }

    public byte[] sendGet(String url, Map<String, String> params, boolean dumpRequest) {
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
            long timeStamp = System.currentTimeMillis();
            if (dumpRequest) {
                dumpHttpRequest(urlSb, timeStamp);
            }
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
                    String val = ObjectHelper.toString(me.getValue(), "");
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

    public void addFileContentChanges(String path, byte[] content, boolean create) {
        FileSystem.addFileContentChanges(path, content, create, changes);
    }

    public static void addFileContentChanges(String path, byte[] content, boolean create, Map<String, Object> changes) {
        addFileContentChanges(path, content, false, create, changes);
    }

    public static void addFileContentChanges(String path, byte[] content, boolean binary, boolean create, Map<String, Object> changes) {

        path = StringHelper.normalizePath(path);
        Object contentObj = content;
        if (!binary) {
            String contentStr = (content == null ? "" : new String(content));
            contentObj = contentStr;
        }
        if (!create) {
            changes.put(path + "/jcr:content/jcr:data", contentObj);
        } else {
            changes.put("+/" + path, "{\"jcr:primaryType\":\"nt:file\"}");
            changes.put("+/" + path + "/jcr:content", "{\"jcr:primaryType\":\"nt:resource\"}");
            changes.put(path + "/jcr:content/jcr:data", contentObj);
            changes.put(path + "/jcr:content/jcr:mimeType", MimeTypeHelper.BINARY);
            changes.put(path + "/jcr:content/jcr:encoding", "utf-8");
        }
        changes.put(path + "/jcr:content/jcr:lastModified", new Date());
    }

    public void setFileContent(String path, byte[] content, boolean binary) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new HashMap<String, Object>();
            addFileContentChanges(path, content, binary, false, changes);
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

    private static void createFileWithChanges(String path, byte[] content, String mimeType, Map<String, Object> changes) {
        if (content == null) {
            content = "".getBytes();
        }
        createNode(path, "nt:file", changes);
        String contentPath = path + "/jcr:content";
        createNode(contentPath, "nt:resource", changes);
        if (mimeType != null) {
                setNodeAttribute(contentPath, "jcr:mimeType", mimeType, changes);
            }
        setNodeAttribute(contentPath, "jcr:data", content, changes);
    }

    public static void createFolder(String path, Map<String, Object> changes) {
        createNode(path, NodeTypeSet.SLING_FOLDER, changes);
    }

    public void createFile(String path, byte[] content, String mimeType) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new LinkedHashMap<String, Object>();
            createFileWithChanges(path, content, mimeType,changes);
            
            sendPost(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    public void createFile(String path, byte[] content) {
        createFile(path, content, null);
    }

    public void createFile(String path, String content) {
        path = StringHelper.normalizePath(path);
        try {
            Map<String, Object> changes = new LinkedHashMap<String, Object>();
            createFileWithChanges(path, content.getBytes(), "text/plain",changes);
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
        try {
            String authString = name + ":" + password;
            LogHelper.logInfo(this, "auth string: " + authString);
            String authStringEnc = Base64.encodeBytes(authString.getBytes());
            LogHelper.logInfo(this, "Base64 encoded auth string: " + authStringEnc);
            method.addHeader("Authorization", "Basic " + authStringEnc);
            //method.addHeader(new BasicScheme().authenticate(credentials, (HttpRequest) method));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] getData(String urlPart) {
        return getData(rootUrl, urlPart);
    }

    private byte[] getData(String serverPart, String urlPart) {
        byte[] res = null;
        try {
            DefaultHttpClient httpclient = this.getHttpClient();

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
            HttpGet get = new HttpGet(url);
            configureAuth(get);
            LogHelper.logInfo(this, "Executing request %s", get.getRequestLine());
            HttpResponse response = httpclient.execute(get);
            int code = response.getStatusLine().getStatusCode();
            LogHelper.logInfo(this, "Status code %d", code);
            if (code == 200) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                res = IOHelper.readInputStreamToBytes(is);
                is.close();
            }
            get.releaseConnection();
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
        if (value instanceof byte[]) {
            byte[] bytes = (byte[]) value;
            return generateBinaryBody(key, bytes);
        } else {
            return generateTextBody(key, value);
        }
    }

    private ContentBody generateBinaryBody(String key, byte[] data) {
        String mimeType = MimeTypeHelper.BINARY;
        ByteArrayBody res = new ByteArrayBody(data, key);
        return res;
    }

    private ContentBody generateTextBody(String key, Object value) throws Exception {
        String mimeType = MimeTypeHelper.TEXT;
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
        if (!LogHelper.disableHttpLogs) {
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
    }

    private byte[] getResponseBytes(HttpEntity resEntity, long timeStamp) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOHelper.readInputStreamToOutputStream(resEntity.getContent(), bos);
        if (!LogHelper.disableHttpLogs) {
            FileOutputStream fos = new FileOutputStream(LogHelper.HTTP_PREFIX + "/" + timeStamp + ".response.log");
            fos.write(bos.toByteArray());
            fos.close();
        }
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

    public void addRelatedObject(Disposable obj) {
        this.relatedObjects.add(obj);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public FileSystemServer getServer() {
        return server;
    }

    public String getProtocol() {
        return protocol;
    }

    public void dispose() {
        for (Disposable obj : relatedObjects) {
            try {
                obj.dispose();
            } catch (Exception ex) {
                LogHelper.logError(ex);
            }
        }
    }

    public String getClipboardContent() {
        return clipboard;
    }

    public boolean isClipboardCopy() {
        return clipboardCopy;
    }

    public void setClipboardContent(String clipboard, boolean clipboardCopy) {
        this.clipboard = clipboard;
        this.clipboardCopy = clipboardCopy;
    }

    private void moveCopyNode(String src, String dest, boolean copy) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(":operation", copy ? "copy" : "move");
        if (!dest.endsWith("/")) {
            dest = dest + "/";
        }
        if (!src.startsWith("/")) {
            src = "/" + src;
        }
        if (!dest.startsWith("/")) {
            dest = "/" + dest;
        }
        params.put(":dest", dest);

        byte[] data = this.sendPost(src, null, params);
        LogHelper.logInfo(this, new String(data));
    }

    public void moveNode(String src, String dest) {
        moveCopyNode(src, dest, false);

    }

    public void copyNode(String src, String dest) {
        moveCopyNode(src, dest, true);

    }

}
