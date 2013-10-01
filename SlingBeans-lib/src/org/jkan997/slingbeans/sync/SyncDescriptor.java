/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.sync;

import java.io.File;
import org.jkan997.slingbeans.helper.StringHelper;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import static org.jkan997.slingbeans.sync.Synchronizer.SYNC_DESCRIPTOR;

/**
 *
 * @author jakaniew
 */
public class SyncDescriptor implements Serializable {

    private final static Comparator comp = new SyncEntryComparator();
    private String bundlePath = null;
    private String remotePath = null;
    private String remoteFsId = null;
    private long lastSync = -1;
    private Map<String, SyncEntry> syncEntryMap = new TreeMap<String, SyncEntry>();

    public static File findSyncDescriptor(File f) {
        File syncFile = null;
        while (f != null) {
            syncFile = new File(f.getAbsolutePath() + "/" + SYNC_DESCRIPTOR);
            if (syncFile.exists()) {
                return f;
            }
            f = f.getParentFile();
        }
        return null;
    }
    
    
    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public Set<Map.Entry<String, SyncEntry>> entrySet() {
        return syncEntryMap.entrySet();
    }

    public SyncEntry[] entries() {
        SyncEntry[] arr = syncEntryMap.values().toArray(new SyncEntry[]{});
        Arrays.sort(arr, comp);
        return arr;
    }
    
    public void clear(){
        syncEntryMap.clear();
    }

    public synchronized SyncEntry getSyncEntry(String path, boolean folder) {
        path = StringHelper.normalizePath(path);
        SyncEntry res = syncEntryMap.get(path);
        if (res == null) {
            res = new SyncEntry(path);
            res.setFolder(folder);
            syncEntryMap.put(path, res);
        }
        return res;
    }

    public Map<String, SyncEntry> getSyncEntryMap() {
        return syncEntryMap;
    }

    public void setSyncEntryMap(Map<String, SyncEntry> syncEntryMap) {
        this.syncEntryMap = syncEntryMap;
    }

    public String getBundlePath() {
        return bundlePath;
    }

    public void setBundlePath(String bundlePath) {
        this.bundlePath = bundlePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteFsId() {
        return remoteFsId;
    }

    public void setRemoteFsId(String remoteFsId) {
        this.remoteFsId = remoteFsId;
    }
    
    
}
