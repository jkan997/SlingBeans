/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.sync;

import org.jkan997.slingbeans.slingfs.FileObject;
import java.io.File;

/**
 *
 * @author jkan997
 */
public class SyncScanner {

    private SyncDescriptor map;
    private boolean remote = false;

    public void scan(FileObject fo, SyncDescriptor map) {
        String rootDir = fo.getPath();
        SynchronizableFile sf = new FileObjectSF(fo, rootDir);
        remote = true;
        scan(sf, map);
    }

    public void scan(File f, SyncDescriptor map) {
        remote=false;
        String rootDir = f.getPath();
        SynchronizableFile sf = new FileSF(f, rootDir);
        scan(sf, map);
    }

    public void scan(SynchronizableFile sf, SyncDescriptor map) {
        this.map = map;
        scanFileObject(sf);
    }

    private void scanFileObject(SynchronizableFile sf) {
        if (sf.path.equals(Synchronizer.SYNC_DESCRIPTOR)) return;
        boolean isFile = sf.isFile();
        boolean isFolder = sf.isFolder();
        if ((isFile) || (isFolder)) {
            SyncEntry se = map.getSyncEntry(sf.getPath(), isFolder);
            if (remote) {
                se.setModifiedRemote(sf.lastModified());
            } else {
                se.setModifiedLocally(sf.lastModified());
            }
        }
        if (sf.isFolder()) {
            for (SynchronizableFile child : sf.getChildren()) {
                scanFileObject(child);
            }
        }
    }
}
