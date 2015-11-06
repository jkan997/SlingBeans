/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jkan997.slingbeans.helper.FileNameHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.PropertyType;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.jkan997.slingbeans.slingfs.SlingFileObject;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.w3c.dom.Element;

/**
 *
 * @author jakaniew
 */
public class LocalFileObject implements SlingFileObject {

    private String name;
    private String ext;
    private String docViewPath;
    private boolean isFolder;
    private boolean root = false;
    private String filePath = null;
    private String localFilePath = null;
    private final List<LocalFileObject> childrenList = new ArrayList<LocalFileObject>();
    private final LocalFileSystem fileSystem;
    private Element xmlElement;

    public LocalFileObject(LocalFileSystem lfs) {
        this.name = "ROOT";
        this.root = true;
        this.fileSystem = lfs;
    }

    public LocalFileObject(String fullName, LocalFileSystem lfs) {
        String[] nameExt = FileNameHelper.getNameExt(fullName);
        this.name = nameExt[0];
        this.ext = nameExt[1];
        this.fileSystem = lfs;
    }

    public String getFullName() {
        return name + (((ext != null) && (ext.length() > 0)) ? "." + ext : "");
    }

    public void addChild(LocalFileObject lfo) {
        if (lfo.name.contains("config")){
            System.out.println("Ss");
        }
        if (!hasChild(lfo.getFullName())) {
            String parentPath = this.getFilePath();
           childrenList.add(lfo);
           
            // TODO : Check why this path is null sometimes
           // if (parentPath != null) 
           
                System.out.println(parentPath);

                if (parentPath.equals("/")) {
                    lfo.setFilePath("/" + lfo.getFullName());
                } else {
                    lfo.setFilePath(parentPath + "/" + lfo.getFullName());
                }
            
        }
    }

    public boolean hasChild(String name) {
        return (getChild(name) != null);
    }

    public LocalFileObject getChild(String name) {
        for (LocalFileObject lfo : childrenList) {
            if (name.equals(lfo.getFullName())) {
                return lfo;
            }
        }
        return null;
    }

    public boolean isLeafNode() {
        return childrenList.isEmpty();
    }

    public LocalFileObject getOrCreateChild(String name) {
        if (name.equals("sling")){
            System.out.println(name);
        }
        LogHelper.logInfo(this, " getOrCreateChild %s (current path: %s)",name,this.getFilePath());
        LocalFileObject childFo = getChild(name);
        if (childFo == null) {
            childFo = new LocalFileObject(name, this.fileSystem);
            this.addChild(childFo);
        }
        return childFo;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    private final Map<String, FileObjectAttribute> attributes = new LinkedHashMap<String, FileObjectAttribute>();

    @Override
    public void setAttribute(String key, Object value) throws IOException {
        setAttribute(key, value, 0);
    }

    @Override
    public void setAttribute(String key, Object value, int type) throws IOException {
        LogHelper.logInfo(this, "setAttribute(%s, %s)", key, value);
        FileObjectAttribute foa = null;
        if (attributes.containsKey(key)) {
            foa = attributes.get(key);
            foa.setValue(value);
        } else {
            foa = new FileObjectAttribute(key, value, type);
            attributes.put(key, foa);
        }
        foa.setModified(true);
    }

    public void addAttribute(FileObjectAttribute foa) {
        attributes.put(foa.getPropertyName(), foa);
    }

    @Override
    public Enumeration<String> getAttributes() {
        LogHelper.logInfo(this, "getAttributes()");
        Enumeration<String> res = Collections.enumeration(this.attributes.keySet());
        return res;
    }

    @Override
    public FileObjectAttribute getAttribute(String key) {
        FileObjectAttribute res = attributes.get(key);
        LogHelper.logInfo(this, "getAttribute(%s)", key);
        return res;

    }

    public Map<String, FileObjectAttribute> getAttributesMap() {
        return attributes;
    }

    public LocalFileObject[] getChildren() {
        return childrenList.toArray(new LocalFileObject[]{});
    }

    public String getPrimaryType() {
        String res = null;
        if (attributes.containsKey("jcr:primaryType")) {
            res = attributes.get("jcr:primaryType").getValue().toString();
        }
        return res;
    }

    public void setPrimaryType(String value) {
        String res = null;
        String propName = "jcr:primaryType";
        FileObjectAttribute foa = new FileObjectAttribute(propName, value, PropertyType.STRING);
        foa.setReadOnly(true);
        attributes.put(propName, foa);
    }

    public boolean isSlingFolder() {
        String primaryType = getPrimaryType();
        if ((primaryType != null) && (NodeTypeSet.FOLDER_TYPES.contains(primaryType))) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isSlingFile() {
        String primaryType = getPrimaryType();
        if ((primaryType != null) && (NodeTypeSet.FILE_TYPES.contains(primaryType))) {
            return true;
        } else {
            return false;
        }

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void saveAttributes() {
        LogHelper.logInfo(this, "Saving attributes");
        if (xmlElement != null) {
            for (Map.Entry<String, FileObjectAttribute> me : this.attributes.entrySet()) {
                String name = me.getKey();
                FileObjectAttribute foa = me.getValue();
                if (foa.isModified()) {
                    LogHelper.logInfo(this, "Set property %s=%s", name, me.getValue().getXmlValue());
                    xmlElement.setAttribute(name, foa.getXmlValue());
                }
            }
            fileSystem.saveXmlElement(xmlElement);

        }

    }

    public String getXmlDocumentPath() {
        File xmlDocumentFile = fileSystem.getXmlDocumentPath(xmlElement);
        if (xmlDocumentFile != null) {
            return xmlDocumentFile.getPath();
        } else {
            return null;
        }
    }

    void setXmlElement(Element xmlElement) {
        this.xmlElement = xmlElement;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

}
