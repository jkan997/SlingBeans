/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author jakaniew
 */
public class FileSF extends SynchronizableFile {
    private File file;

    public FileSF(File file, String rootPath) {
        this.file = file;
        setRootDir(rootPath);
        this.setFullPath(file.getPath());
    }

    @Override
    public SynchronizableFile[] getChildren() {
        if (!isFolder()) return new SynchronizableFile[]{};
        File[] files = file.listFiles();
        int len = files.length;
        SynchronizableFile[] res = new SynchronizableFile[len];
        for (int i = 0;i<len;i++){
            File f = files[i];
            SynchronizableFile sf = new FileSF(f,rootDir);
            res[i]=sf;
            
        }
        
        return res;
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public boolean isFolder() {
        return file.isDirectory();
    }

    public boolean isFile(){
        return !file.isDirectory();
    }
    
    @Override
    public byte[] getContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createFile(String name, byte[] content) {
        try {
            String newFilePath = file.getPath()+"/"+name;
            File newFile = new File(newFilePath);
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(content);
            fos.close();
        } catch (IOException ex) {
           throw new RuntimeException(ex);
        }
    }

    @Override
    public void createFolder(String name, byte[] content) {
        String newFolderPath = file.getPath()+"/"+name;
        File newFolder = new File(newFolderPath);
        newFolder.mkdir();
    }

    
    
}
