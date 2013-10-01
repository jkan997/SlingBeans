/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs;

import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.StringHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jkan997.slingbeans.slingfs.types.NodeType;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.util.Enumerations;

/**
 *
 * @author jakaniew
 */
public class FileObject extends org.openide.filesystems.FileObject {

  

    class FileObjectOutputStream2 extends ByteArrayOutputStream {

        @Override
        public void close() throws IOException {
            super.close();
            FileObject.this.setFileContent(this.toByteArray());
        }
    }
    private FileObject jcrContent = null;
    private String path = "";
    private FileSystem fs;
    private String name = "";
    private String ext = "";
    private boolean folder;
    private byte[] fileContent;
    private FileObject[] children = FileSystem.EMPTY_FO_ARR;
    boolean childrenLoaded = false;
    private final Map<String, FileObjectAttribute> attributes = new LinkedHashMap<String, FileObjectAttribute>();
    private long syncTimestamp = 0;
    private ListenerList listeners;

    FileObject() {
    }

    public FileObject(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExt() {
        return ext;
    }

    @Override
    public void rename(FileLock fl, String s1, String s2) throws IOException {
        LogHelper.logInfo(this, "rename(%s, %s, %s)", fl, s1, s2);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileSystem getFileSystem() throws FileStateInvalidException {
        LogHelper.logInfo(this, "getFileSystem()");
        return fs;
    }

    public String getFileSystemId() {
        return fs.getFileSystemId();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    private String getParentPath() {
        String res = StringHelper.getParentPath(path);
        return res;

    }

    @Override
    public FileObject getParent() {
        String parentPath = getParentPath();
        LogHelper.logInfo(this, "getParent(%s) / parentPath - %s", path, parentPath);
        FileObject res = null;
        if (parentPath != null) {
            res = fs.getFileObject(parentPath);
        }
        LogHelper.logInfo(this, "Parent %s", "" + res);
        return res;
    }

    @Override
    public boolean isFolder() {
        LogHelper.logInfo(this, "isFolder(%s)");
        return folder;
    }

    public FileObject getJcrContent() {
        if (!this.childrenLoaded) {
            this.getChildren();
        }
      
        return jcrContent;
    }

    public void refreshJcrContent() {
        fs.getFileObject(jcrContent, true);
    }

    public boolean hasContent() {
        return (getJcrContent() != null);
    }

    public Date getCreated() {
        FileObjectAttribute foa = null;
        foa = getAttribute("jcr:created");
        if (foa != null) {
            Date created = foa.getDate();
            if (created != null) {
                return created;
            }
        }
        return null;
    }

    @Override
    public Date lastModified() {
        return lastModified(true);
    }

    public Date lastModified(boolean forceRefresh) {
        FileObjectAttribute foa = null;
        Date res = new Date(2013, 0, 1);
        Date created = getCreated();
        if (created != null) {
            res = created;
        }
        if (jcrContent != null) {
            if (forceRefresh) {
                refreshJcrContent();
            }
            foa = jcrContent.getAttribute("jcr:lastModified");
            if (foa != null) {
                Date lastModified = foa.getDate();
                if (lastModified != null) {
                    res = lastModified;
                }
            }
        }
        LogHelper.logInfo(this, "lastModified() = %s", ("" + res));

        return res;
    }

    @Override
    public boolean isRoot() {
        String parentPath = getParentPath();

        boolean res = (parentPath == null);
        LogHelper.logInfo(this, "parentPath() = %s", ("" + res));
        return res;
    }

    @Override
    public boolean isData() {
        boolean res = !folder;
        LogHelper.logInfo(this, "isData() = %s", "" + res);
        return res;
    }

    @Override
    public boolean isValid() {
        LogHelper.logInfo(this, "isValid(%s)", path);
        return true;
    }

    @Override
    public void delete(FileLock fl) throws IOException {
        LogHelper.logInfo(this, "delete(%s)", fl);
        /* Map<String, Object> changes = new HashMap<String, Object>();
         changes.put("-/" + this.getPath(),"");
         fs.sendPost(changes);*/
        fs.remove(this.path);
    }

    @Override
    public FileObjectAttribute getAttribute(String key) {
        FileObjectAttribute res = attributes.get(key);
        LogHelper.logInfo(this, "getAttribute(%s)", key);
        return res;

    }

    @Override
    public void setAttribute(String key, Object value) throws IOException {
        LogHelper.logInfo(this, "setAttribute(%s, %s)", key, value);
        FileObjectAttribute foa = null;
        if (attributes.containsKey(key)) {
            foa = attributes.get(key);
        } else {
            foa = new FileObjectAttribute();
            attributes.put(key, foa);
        }
        foa.setValue(value);
        foa.setModified(true);
    }

    @Override
    public Enumeration<String> getAttributes() {
        LogHelper.logInfo(this, "getAttributes()");
        Enumeration<String> res = Collections.enumeration(this.attributes.keySet());
        return res;
    }

    @Override
    public final synchronized void addFileChangeListener(FileChangeListener fcl) {
        if (listeners == null) {
            listeners = new ListenerList<FileChangeListener>();
        }
        listeners.add(fcl);
    }

    @Override
    public final synchronized void removeFileChangeListener(FileChangeListener fcl) {
        if (listeners != null) {
            listeners.remove(fcl);
        }
    }

    private final Enumeration<FileChangeListener> listeners() {
        if (listeners == null) {
            return Enumerations.empty();
        } else {
            return Collections.enumeration(listeners.getAllListeners());
        }
    }

    @Override
    public long getSize() {
        long res = 0;
        if (fileContent != null) {
            res = fileContent.length;
        }
        if (jcrContent != null) {
            refreshJcrContent();
            FileObjectAttribute foa = jcrContent.getAttribute(":jcr:data");
            if (foa != null) {
                Long dataSize = foa.getLong();
                if (dataSize != null) {
                    res = dataSize;
                }
            }
        }
        LogHelper.logInfo(this, "getSize() = %s", "" + res);
        return res;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        LogHelper.logInfo(this, "getInputStream()");
        getFileContent();
        InputStream res = null;
        LogHelper.logInfo(this, "File content len %s", "" + (fileContent != null ? fileContent.length : -1));
        res = new ByteArrayInputStream(fileContent);
        return res;
    }

    public byte[] getFileContent() {
        if (this.fileContent == null) {
            this.fileContent = fs.getFileContent(path);
        }
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
        fs.setFileContent(this.path, this.fileContent);
        objectModified();
    }

    @Override
    public OutputStream getOutputStream(FileLock fl) throws IOException {
        LogHelper.logInfo(this, "getOutputStream()");
        return new FileObjectOutputStream2();
    }

    @Override
    public FileLock lock() throws IOException {
        LogHelper.logInfo(this, "lock()");
        return FileLock.NONE;
    }

    @Override
    public void setImportant(boolean val) {
        LogHelper.logInfo(this, "setImportant(%s)", "" + val);
    }

    @Override
    public FileObject[] getChildren() {
        LogHelper.logInfo(this, "getChildren() = %s elements", children.length);
        if ((!childrenLoaded) && (!fs.isNoLoadMode())) {
            fs.getFileObject(this.getPath(), 2);
        }
        childrenLoaded = true;
        return children;
    }

    public void setChildren(List<FileObject> childrenList) {
        jcrContent = null;
        if (childrenList == null) {
            this.children = FileSystem.EMPTY_FO_ARR;
            this.childrenLoaded = false;
        } else {
            FileObject[] childrenArr = childrenList.toArray(FileSystem.EMPTY_FO_ARR);
            boolean sortChildren = false;
            if (this.getPath().length() > 0) {
                sortChildren = true;
                NodeTypeSet nodeTypeSet = fs.getNodeTypes();
                if (nodeTypeSet != null) {
                    NodeType nt = nodeTypeSet.getByName(this.getPrimaryType());
                    if ((nt != null) && (nt.isHasOrderableChildNodes())) {
                        sortChildren = false;
                    }
                }
            }
            if (sortChildren) {
                Arrays.sort(childrenArr, FileObjectComparator.instance);
            }
            for (FileObject child : childrenArr) {
                if (child.getName().equals("jcr:content")) {
                    jcrContent = child;
                }
            }
            this.children = childrenArr;
            this.childrenLoaded = true;
        }
    }

    @Override
    public FileObject getFileObject(String s1, String s2) {
        LogHelper.logInfo(this, "getFileObject(%s, %s)", s1, s2);
        return null;
    }

    @Override
    public FileObject createFolder(String s1) throws IOException {
        LogHelper.logInfo(this, "createFolder(%s, %s)", s1);
        return null;
    }

    @Override
    public FileObject createData(String name, String ext) throws IOException {
        LogHelper.logInfo(this, "createData(%s, %s)", name, ext);
        String nodeName = null;
        if ((ext != null) && (ext.length() > 0)) {
            nodeName = name + "." + ext;
        } else {
            nodeName = name;
        }
        String path = this.path + "/" + nodeName;
        fs.setFileContent(path, null, true);
        return null;
    }

    public void createNode(String nodeName, String nodeType) throws IOException {
        LogHelper.logInfo(this, "createNode(%s, %s)", name, nodeType);
        String path = this.path + "/" + nodeName;
        fs.createNode(path, nodeType);
        // fs.commmit();
    }

    @Override
    public boolean isReadOnly() {
        LogHelper.logInfo(this, "isReadOnly()");
        return false;
    }

    void setPath(String path) {
        this.path = path;
    }

    void setName(String name) {
        this.name = name;
    }

    void setExt(String ext) {
        this.ext = ext;
    }

    void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getPrimaryType() {
        String res = null;
        if (attributes.containsKey("jcr:primaryType")) {
            res = attributes.get("jcr:primaryType").getValue().toString();
        }
        return res;
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

    public boolean isFile() {
        this.getChildren();
        return (jcrContent != null);
    }

    public Map<String, FileObjectAttribute> getAttributesMap() {
        return attributes;
    }

    public boolean isLeafNode() {
        if ((this.childrenLoaded) && (this.children.length == 0)) {
            return true;
        } else {
            return false;
        }

    }

    public FileObject getClassPathParent() {
        FileObject parent = this;
        FileObject oldParent = this;
        do {
            oldParent = parent;
            parent = parent.getParent();
            if ((parent != null) && (parent.getNameExt().equals("java"))) {
                return parent;
            }
        } while (parent != null);
        return null;
    }

    public synchronized void saveAttributes() {
        FileObjectAttribute foa = null;
        Map<String, Object> changes = new HashMap<String, Object>();
        for (Map.Entry<String, FileObjectAttribute> me : attributes.entrySet()) {
            foa = me.getValue();
            if (foa.isModified()) {
                String key = path + "/" + me.getKey();
                changes.put(key, foa.getValue());
                //changes.put(key+"@TypeHint", "String");
            }
        }
        fs.sendPost(changes);
        for (FileObjectAttribute foa2 : attributes.values()) {
            foa2.setModified(false);
        }
    }

    @Override
    public String toString() {
        if ("".equals(path)) {
            return "/";
        }
        String[] nameExt = StringHelper.extractNameExt(path);
        if (nameExt[1].length() > 0) {
            return nameExt[0] + "." + nameExt[1];
        }
        return nameExt[0];
    }

    public String toStringFull() {
        return toStringFull(", ");
    }

    public String toStringFull(String separator) {
        String parentPath = getParentPath();
        return "FileObject{" + "path=" + path + separator + "fs=" + fs + separator + "name=" + name + separator + "ext=" + ext + separator + "folder=" + folder + separator + "parentPath=" + parentPath + separator + "fileContent=" + fileContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileObject other = (FileObject) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }

    public FileObject(FileSystem fs, boolean folder, byte[] fileContent) {
        this.fs = fs;
        this.folder = folder;
        this.fileContent = fileContent;
    }

    public boolean isChildrenLoaded() {
        return childrenLoaded;
    }

    public void setChildrenLoaded(boolean childrenLoaded) {
        this.childrenLoaded = childrenLoaded;
    }

    public long getSyncTimestamp() {
        return syncTimestamp;
    }

    public void objectModified() {
        syncTimestamp = System.currentTimeMillis();
        FileEvent fe = new FileEvent(this);
        this.fireFileChangedEvent(listeners(), fe);
    }
}
