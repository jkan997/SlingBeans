/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.slingfs;

import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.StringHelper;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.openide.filesystems.URLMapper;

/**
 *
 * @author jkan997
 */
public class FileSystemFactory {

    public static String FS_PROTOCOL = "nbfs";
    public static FileSystemFactory instance = null;
    
    public static synchronized FileSystemFactory getInstance() {
        if (instance == null) {
            instance = new FileSystemFactory();
        }
        return instance;
    }
    private Map<String, FileSystem> fileSystemMap = Collections.synchronizedMap(new HashMap<String, FileSystem>());

    public FileSystemFactory(boolean useSampleFs) {
        if (useSampleFs) {
            FileSystem fsN = new FileSystem();
            this.registerFileSystem(fsN);
            String fsId = fsN.getFileSystemId();
        }

    }

    public FileSystemFactory() {
        this(false);
    }

    public String getTestFileSystemId() {
        if (fileSystemMap.size() >= 1) {
            return fileSystemMap.values().iterator().next().getFileSystemId();
        }
        return null;
    }

    public URL getURL(org.openide.filesystems.FileObject inputFo, int type) {
        URL res = null;
        if (type == URLMapper.INTERNAL) {
            try {
                if (inputFo instanceof FileObject) {
                    FileObject fo = (FileObject) inputFo;
                    FileSystem fs = fo.getFileSystem();
                    String fsId = fs.getFileSystemId();
                    String path = fsId + "/" + fo.getPath() + (fo.isSlingFolder() ? "/" : "");
                    res = new URL(FS_PROTOCOL, "nbhost", -1, path);
                }
                LogHelper.logInfo(this.getClass(), "getUrl(%s,%d) = %s ", inputFo.toString(), type, res);
            } catch (Exception ex) {
                LogHelper.logError(ex);
            }
        }
        return res;
    }

    public FileObject[] getFileObjects(URL url) {
        System.out.println("UP " + url.getPath());
        String[] fsIdPath = StringHelper.extractFsFromPath(url.getPath());
        String fsId = fsIdPath[0];
        FileSystem fs = getFileSystem(fsId);
        if (fs != null) {
            String path = fsIdPath[1];
            FileObject fo = fs.getFileObject(path);
            if (fo != null) {
                return new FileObject[]{fo};
            }

        }
        return null;
    }
    
    public synchronized FileSystem registerFileSystem(FileSystem fileSystem) {
        String fsId = fileSystem.getFileSystemId();
        System.out.println("FSID " + fsId);
        fileSystemMap.put(fsId, fileSystem);
        return fileSystem;
    }

    private void unregisterAllFilesystems() {
        String[] fsIds = fileSystemMap.values().toArray(new String[]{});
        for (String fsId : fsIds) {
            unregisterFileSystem(fsId);
        }
    }

    public synchronized void unregisterFileSystem(String fileSystemId) {
        if (fileSystemMap.containsKey(fileSystemId)) {
            fileSystemMap.remove(fileSystemId);
        }
    }

    public String getInfo() {
        return "INFO: " + this.toString();
    }

    public FileSystem getFileSystem(String fsId) {
        return fileSystemMap.get(fsId);
    }

    public void test() {
    }
}
