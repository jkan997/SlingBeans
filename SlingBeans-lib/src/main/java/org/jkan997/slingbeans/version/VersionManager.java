/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.version;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jkan997.slingbeans.helper.HttpClientHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author jakaniew
 */
public class VersionManager {

    public static String CURRENT_VERSION = "1.1";
    public static String VERSION_URL = "https://raw.githubusercontent.com/jkan997/SlingBeans/master/dist/version.xml";

    static long versionToLong(String versionStr) {
        String[] arr = versionStr.split("\\.");
        return Long.parseLong(arr[0]) * 100000 + Long.parseLong(arr[1]);
    }

    public static Version getNewestVersion() {
        Version res = null;
        try {
            HttpClient hc = HttpClientHelper.createHttpClient();
            HttpGet get = new HttpGet(VERSION_URL+"?nocache="+System.currentTimeMillis());
            HttpResponse response = hc.execute(get);
            InputStream is = response.getEntity().getContent();
            res = parseVersion(is);
            is.close();
            get.releaseConnection();
            if (res.getNumericId() <= versionToLong(CURRENT_VERSION)) {
                res = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    private static Version parseVersion(InputStream is) throws SAXException, ParserConfigurationException, IOException {
        Version version = new Version();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        Element rootEl = doc.getDocumentElement();
        NodeList nl = rootEl.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element el = (Element) n;
                if (el != null) {
                    String name = el.getNodeName();
                    String content = el.getTextContent().trim();
                    if (Version.ID.equalsIgnoreCase(name)) {
                        version.setId(content);
                    } else if (Version.DOWNLOAD.equalsIgnoreCase(name)) {
                        version.setDownload(content);
                    } else if (Version.CHANGES.equalsIgnoreCase(name)) {
                        version.setChanges(content);
                    } else if (Version.RELEASED.equalsIgnoreCase(name)) {
                        version.setReleased(content);
                    }
                }
            }
        }
        return version;
    }

}
