/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.sync;

/**
 *
 * @author jakaniew
 */
public class SyncActionHelper {

    public static boolean isRemoteAction(SyncAction action) {
        if (action == SyncAction.COPY_TO_REMOTE) {
            return true;
        }
        if (action == SyncAction.CREATE_FOLDER_REMOTE) {
            return true;
        }
        if (action == SyncAction.REMOVE_REMOTE) {
            return true;
        }
        return false;
    }

    public static boolean isLocalAction(SyncAction action) {
        if (action == SyncAction.COPY_TO_LOCAL) {
            return true;
        }
        if (action == SyncAction.CREATE_FOLDER_LOCAL) {
            return true;
        }
        if (action == SyncAction.REMOVE_LOCAL) {
            return true;
        }
        return false;
    }

    public static boolean isRemovalAction(SyncAction action) {
        if (action == SyncAction.REMOVE_LOCAL) {
            return true;
        }
        if (action == SyncAction.REMOVE_REMOTE) {
            return true;
        }
        return false;
    }
}
