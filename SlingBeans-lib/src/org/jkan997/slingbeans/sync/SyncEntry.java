/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jakaniew
 */
public class SyncEntry implements Serializable {
    private String path;
    private long modifiedRemote = -1;
    private long modifiedLocally = -1;
    private long lastSync = -1;
    private int depth;
    private boolean folder;
    private List<SyncAction> actions = new ArrayList<SyncAction>();

    public SyncEntry(String path) {
        this.path = path;
        int res = 0;
        for (int i = 0, len = path.length(); i < len; i++) {
            char c = path.charAt(i);
            if (c == '/') {
                res++;
            }

        }
        this.depth=res;
    }

    public long getModifiedRemote() {
        return modifiedRemote;
    }

    public void setModifiedRemote(long modifiedRemote) {
        this.modifiedRemote = modifiedRemote;
    }

    public long getModifiedLocally() {
        return modifiedLocally;
    }

    public void setModifiedLocally(long modifiedLocally) {
        this.modifiedLocally = modifiedLocally;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public String getPath() {
        return path;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }
    
     public int getActionCount() {
        return actions.size();
    }

    public SyncAction getAction(SyncMode mode) {
        if (((mode==null)||(mode==SyncMode.MERGE))&&(actions.size()==1)){
            return actions.get(0);
        }
        if (mode==SyncMode.USE_LOCAL_CHANGES){
            for (SyncAction action : actions){
                if (SyncActionHelper.isLocalAction(action)){
                    return action;
                }
            }
        }
        if (mode==SyncMode.USE_REMOTE_CHANGES){
            for (SyncAction action : actions){
                if (SyncActionHelper.isRemoteAction(action)){
                    return action;
                }
            }
        }
        return null;
    }
    
    public SyncAction[] getActions() {
        return actions.toArray(new SyncAction[]{});
    }
    
    public String getActionsStr() {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (SyncAction action : actions){
            if (first){
                first=false;
            } else {
                sb.append(", ");
            }
            sb.append(action);
        }
        return sb.toString();
    }

    public void addAction(SyncAction action) {
        this.actions.add(action);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SyncEntry() {
    }
    
    
    

}
