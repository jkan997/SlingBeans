/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author jakaniew
 */
public class DescriptorXmlHelper {

    private final static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private final static TransformerFactory tFactory = TransformerFactory.newInstance();

    public static Document parse(InputStream is) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public static void save(Document doc, File documentFile) throws TransformerConfigurationException, FileNotFoundException, TransformerException, IOException {
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        FileOutputStream fos = new FileOutputStream(documentFile);
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fos);
        transformer.transform(source, result);
        fos.close();
    }
}
