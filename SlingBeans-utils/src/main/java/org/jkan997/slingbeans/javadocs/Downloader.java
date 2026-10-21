/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.javadocs;

import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author jakaniew
 */
public class Downloader {

    private final DefaultHttpClient httpClient;
    public String rootDir;
    public String rootUrl;

    public void createFolder(String relPath, boolean skipLast) {
        String[] arr = relPath.split("/");
        String dir = "";
        for (int i = 0; i < (skipLast ? arr.length - 1 : arr.length); i++) {
            dir += "/" + arr[i];
            File dirFile = new File(rootDir + "/" + dir);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
        }
    }

    public Downloader() {
        httpClient = new DefaultHttpClient();
    }

    public String getString(String url) throws IOException {
        String res = null;
        LogHelper.logInfo(this, url);
        StringBuilder diff = new StringBuilder();
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        int code = response.getStatusLine().getStatusCode();
        LogHelper.logInfo(this, "Status code %d", code);
        if (code == 200) {
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            res = IOHelper.readInputStreamToString(is);
            is.close();
        }

        return res;
    }

    public void downloadSingleFile(String relPath) throws Exception {
        String fileUrl = rootUrl + relPath;
        createFolder(relPath, true);
        File diskFile = new File(rootDir + "/" + relPath);
        if ((diskFile.exists()) && (diskFile.length() > 1000)) {
            LogHelper.logInfo(this, "File already exists %s", fileUrl);
        } else {
            LogHelper.logInfo(this, "Downloading file %s", fileUrl);
            String fileStr = getString(fileUrl);
            FileWriter fw = new FileWriter(diskFile);
            fw.append(fileStr);
            fw.close();
            Thread.sleep(1000);
        }

    }

    public void download(String rootUrl) throws Exception {
        rootUrl = rootUrl.replaceAll("index.html", "");
        rootUrl = rootUrl.replaceAll("allclasses-frame.html", "");
        if (!rootUrl.endsWith("/")) {
            rootUrl += "/";
        }
        this.rootUrl = rootUrl;
        String mainUrl = rootUrl + "allclasses-frame.html";
        LogHelper.logInfo(this, "Downloading root file %s", mainUrl);
        String rootStr = getString(mainUrl);
        FileWriter fw = new FileWriter(rootDir + "/allclasses-frame.html");
        fw.append(rootStr);
        fw.close();
        //downloadSingleFile("index-all.html");
        //downloadSingleFile("overview-summary.html");
        downloadSingleFile("index-files.html");
        downloadSingleFile("package-list");

        String[] hrefArr = rootStr.split("(HREF|href)=\"");
        int hrefLen = hrefArr.length;
        for (int i = 1; i < hrefLen; i++) {
            LogHelper.logInfo(this, "Progress %d (%d from %d)", Math.round(i * 100 / hrefLen), i, hrefLen);
            if (1 == 1) {
                break;
            }
            String s = hrefArr[i];
            if (s.startsWith("http:")) {
                LogHelper.logInfo(this, "Remote link %s", s);
                continue;
            }
            int ind = s.indexOf("\"");
            s = s.substring(0, ind);
            hrefArr[i] = s;
            try {
                downloadSingleFile(s);
            } catch (Exception ex) {
                System.out.println("Error fetching file " + s);
            }
            System.out.println(s);
        }

        //System.out.println(rootStr);
    }

    public static void main(String[] args) throws Exception {
        LogHelper.printToConsole = true;
        Downloader d = new Downloader();
       // d.rootDir = "/tmp/day";
       // d.download("http://dev.day.com/docs/en/cq/current/javadoc/allclasses-frame.html");
        
        d.rootDir = "/tmp/jcr";
        d.download("http://www.day.com/maven/jsr170/javadocs/jcr-2.0/allclasses-frame.html");
    }
}
