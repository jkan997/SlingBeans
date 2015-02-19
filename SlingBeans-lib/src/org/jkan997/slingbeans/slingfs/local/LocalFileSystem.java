/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jakaniew
 */
public class LocalFileSystem {

    private String jcrRootPath;
    private LocalFileObject rootFileObject;
    private String contentPath;
    private Map<Document, File> xmlDescriptorFiles = new HashMap<Document, File>();

    public void refresh() {
        xmlDescriptorFiles.clear();
        rootFileObject = new LocalFileObject(this);
        rootFileObject.setFilePath("/");
        File jcrRootFolder = new File(jcrRootPath);
        scanFolder(jcrRootFolder, rootFileObject);
    }

    public LocalFileObject getFileObject(String path) {
        if (path.startsWith(this.jcrRootPath)) {
            path = path.substring(jcrRootPath.length());
            System.out.println(path);
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] pathArr = path.split("/");
        LocalFileObject lfo = rootFileObject;
        for (String pathPart : pathArr) {
            LocalFileObject childLfo = lfo.getChild(pathPart);
            if (childLfo == null) {
                return null;
            }
            lfo = childLfo;
        }
        return lfo;
    }

    public void setContentPath(String contentPath) {
        this.jcrRootPath = contentPath + "/jcr_root";
        this.contentPath = contentPath;
        refresh();
    }

    public String getContentPath() {
        return contentPath;
    }

    public LocalFileObject getRootFileObject() {
        return rootFileObject;
    }

    private static void print(int level, LocalFileObject lfo) {
        String s = "";
        int spacing = level * 2;
        String spacingStr = "";
        for (int i = 0; i <= spacing; i++) {
            spacingStr += " ";
        }
        System.out.println(spacingStr + lfo.getFullName() + "[folder:" + lfo.isSlingFolder() + ", file:" + lfo.isSlingFile() + "] [" + lfo.getFilePath() + "]");
        Map<String, FileObjectAttribute> attrsMap = lfo.getAttributesMap();
        for (FileObjectAttribute foa : attrsMap.values()) {
            // System.out.println(spacingStr + " @" + foa.getPropertyName()+"="+foa.getValue());
        }

    }

    private static void printLfo(int level, LocalFileObject lfo) {
        print(level, lfo);
        for (Object fo : lfo.getChildren()) {
            LocalFileObject clfo = (LocalFileObject) fo;
            printLfo(level + 1, clfo);
        }
    }

    public static void main(String[] args) throws IOException {
        String jcrRoot = "/Volumes/MacData/jakaniew/git/test-content/src/main/content";
        LocalFileSystem lfs = new LocalFileSystem();
        lfs.setContentPath(jcrRoot);
        // lfs.getFileObject(jcrRoot)
        printLfo(0, lfs.getFileObject("/Volumes/MacData/jakaniew/git/test-content/src/main/content/jcr_root/apps"));
        LocalFileObject lfo2 = lfs.getFileObject("/apps/dsw/workflow/createproduct/cq:editConfig/cq:formParameters");
        System.out.println(lfo2);
        lfo2.setAttribute("sss", "ccc");
        lfo2.saveAttributes();
        System.out.println(lfo2.getFilePath());
    }

    private void scanFolder(File folder, LocalFileObject lfo) {
        File contentXmlFile = new File(folder.getPath() + "/.content.xml");
        if (contentXmlFile.exists()) {
            scanXml(contentXmlFile, lfo, false);
        }
        File[] files = folder.listFiles();
        String fileName;
        if (files != null) {
            for (File childFile : files) {
                fileName = childFile.getName();
                if ((!fileName.equals(".content.xml")) && (fileName.endsWith(".xml"))) {
                    scanXml(childFile, lfo, true);
                }
            }
            for (File childFile : files) {
                if (isXmlDescriptorFile(childFile)) {
                    continue;
                }
                fileName = childFile.getName();
                fileName = normalizeNameUnderscores(fileName);
                LocalFileObject childLfo = lfo.getOrCreateChild(fileName);
                if (childFile.isDirectory()) {
                    childLfo.setIsFolder(true);
                    scanFolder(childFile, childLfo);
                }
                if (childLfo.getPrimaryType() == null) {
                    childLfo.setPrimaryType(childFile.isDirectory() ? NodeTypeSet.NT_FOLDER : NodeTypeSet.NT_FILE);
                }
                childLfo.setLocalFilePath(childFile.getPath());
            }
        }
    }

    private void copyAttributesToLocalFileObject(Element xmlElement, LocalFileObject lfo) {
        NamedNodeMap xmlAttributes = xmlElement.getAttributes();
        int len = xmlAttributes.getLength();
        for (int i = 0; i < len; i++) {
            Attr attr = (Attr) xmlAttributes.item(i);
            String attrName = attr.getName();
            String attrVal = attr.getValue();
            if (attrName.startsWith("xmlns")) {
                continue;
            }
            FileObjectAttribute foa = new FileObjectAttribute();
            foa.setXmlValue(attrVal, attrName);
            lfo.addAttribute(foa);
        }
        lfo.setXmlElement(xmlElement);
    }

    private String normalizeNameUnderscores(String name) {
        if (name.startsWith("_")) {
            name = name.substring(1);
        }
        name = name.replace('_', ':');
        return name;
    }

    private boolean scanXml(File xmlFile, LocalFileObject lfo, boolean createRoot) {
        boolean res = false;
        try {
            InputStream is = new FileInputStream(xmlFile);
            Document doc = DescriptorXmlHelper.parse(is);
            xmlDescriptorFiles.put(doc, xmlFile);
            Element rootEl = doc.getDocumentElement();
            if (rootEl.getNodeName().equals("jcr:root")) {
                if (createRoot) {
                    String childName = xmlFile.getName().replace(".xml", "");
                    childName = normalizeNameUnderscores(childName);
                    lfo = lfo.getOrCreateChild(childName);
                }
                copyAttributesToLocalFileObject(rootEl, lfo);
                processXmlElement(rootEl, lfo);
                res = true;
            }
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
        return res;
    }

    public File getXmlDocumentPath(Element xmlElement) {
        if (xmlElement == null) {
            return null;
        }
        Document doc = xmlElement.getOwnerDocument();
        File documentFile = this.xmlDescriptorFiles.get(doc);
        return documentFile;
    }

    public void saveXmlElement(Element xmlElement) {
        Document doc = xmlElement.getOwnerDocument();
        File documentFile = this.xmlDescriptorFiles.get(doc);
        LogHelper.logInfo(this, "Document node %s, file: %s", doc, documentFile);
        try {
            DescriptorXmlHelper.save(doc, documentFile);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void processXmlElement(Element xmlElement, LocalFileObject parentLfo) {
        System.out.println(xmlElement);
        NodeList nl = xmlElement.getChildNodes();
        System.out.println(xmlElement.getNodeName());
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            System.out.println(n + " " + i + " / " + nl.getLength());
            if (n instanceof Element) {
                Element xmlChild = (Element) n;
                String type = xmlChild.getAttribute("jcr:primaryType");
                if ((type != null) && (!"".equals(type))) {
                    LocalFileObject childLfo = parentLfo.getOrCreateChild(xmlChild.getNodeName());
                    if (childLfo != null) {
                        copyAttributesToLocalFileObject(xmlChild, childLfo);
                        processXmlElement(xmlChild, childLfo);
                    }
                }
            }
        }
    }

    public boolean isXmlDescriptorFile(File childFile) {
        for (File f : xmlDescriptorFiles.values()) {
            if (f.equals(childFile)) {
                return true;
            }
        }
        return false;
    }

}
