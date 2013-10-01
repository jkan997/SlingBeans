/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbservices;


import java.net.URL;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.FileSystemFactory;
import org.openide.filesystems.URLMapper;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jakaniew
 */
@ServiceProvider(service = SlingFsFactory.class)
public class SlingFsFactory extends URLMapper {

    private FileSystemFactory fsFactory;
    private String defaultFileSystemId = null;

    public static SlingFsFactory lookup() {
        Lookup lookup = Lookup.getDefault();
        SlingFsFactory res = lookup.lookup(SlingFsFactory.class);
        
        return res;
    }

    public SlingFsFactory() {
       fsFactory = FileSystemFactory.getInstance();
      
    }
    
    public FileSystem getDefaultFileSystem(){
        if (defaultFileSystemId!=null){
            return getFileSystem(defaultFileSystemId);
        }
        return null;
    }
    
     public String getDefualtFileSystemId(){
        return defaultFileSystemId;
    }
    
    public void setDefualtFileSystemId(String fsId){
        defaultFileSystemId=fsId;
    }

    @Override
    public URL getURL(org.openide.filesystems.FileObject fo, int type) {
        
        return fsFactory.getURL(fo, type);
    }

    @Override
    public FileObject[] getFileObjects(URL url) {
        return fsFactory.getFileObjects(url);
    }

    public void registerFileSystem(FileSystem fileSystem) {
        fsFactory.registerFileSystem(fileSystem);
    }

    public FileSystem getFileSystem(String fsId) {
        return fsFactory.getFileSystem(fsId);
    }
}
