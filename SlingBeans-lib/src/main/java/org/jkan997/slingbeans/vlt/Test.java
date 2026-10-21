/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.vlt;

import java.io.FileInputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jakaniew
 */
public class Test {

    private static void log(Object msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("/Users/jakaniew/svn/SlingBeans/SlingBeans-lib/src/org/jkan997/slingbeans/vlt/test.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XPath xpath = XPathFactory.newInstance().newXPath();
        Document doc = dBuilder.parse(fis);
        
        String id = xpath.evaluate("//execution/@id",doc.getDocumentElement()).toString();
        System.out.println(id);
        //doc.getDocumentElement().getElementsByTagName("executions").item(0;
        /* FileArchive vltFileArchive = new FileArchive(new File("/Volumes/MacData/jakaniew/git/test-content/src/main/content"));
         vltFileArchive.open(false);
         log(vltFileArchive.getJcrRoot().getName());
         vltFileArchive.close();*/
    }
}
