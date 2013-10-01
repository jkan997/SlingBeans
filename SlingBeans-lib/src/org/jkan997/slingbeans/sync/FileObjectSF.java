/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.sync;

import org.jkan997.slingbeans.slingfs.FileObject;

/**
 *
 * @author jakaniew
 */
public class FileObjectSF extends SynchronizableFile {

    private FileObject fo;

    public FileObjectSF(FileObject fo, String rootPath) {
        this.fo = fo;
        setRootDir(rootPath);
        setFullPath(fo.getPath());
    }

    @Override
    public long lastModified() {
        return fo.lastModified(false).getTime();
    }

    @Override
    public SynchronizableFile[] getChildren() {
        if (!isFolder()) {
            return new SynchronizableFile[]{};
        }
        FileObject[] fileObjects = fo.getChildren();
        int len = fileObjects.length;
        SynchronizableFile[] res = new SynchronizableFile[len];
        for (int i = 0; i < len; i++) {
            FileObject childFo = fileObjects[i];
            SynchronizableFile sf = new FileObjectSF(childFo,rootDir);
            res[i] = sf;
        }
        return res;
    }

    @Override
    public boolean isFolder() {
        return fo.isSlingFolder();
    }
    
    @Override
    public boolean isFile() {
        return fo.isFile();
    }

    @Override
    public byte[] getContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createFile(String name, byte[] content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createFolder(String name, byte[] content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
