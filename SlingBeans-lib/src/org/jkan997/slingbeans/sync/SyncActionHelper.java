/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.sync;

/**
 *
 * @author jkan997
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
