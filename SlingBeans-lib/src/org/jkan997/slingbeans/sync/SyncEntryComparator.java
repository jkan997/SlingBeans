/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.sync;

import java.util.Comparator;

/**
 *
 * @author jkan997
 */
public class SyncEntryComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        SyncEntry se1 = (SyncEntry)o1;
        SyncEntry se2 = (SyncEntry)o2;
        int res = Integer.compare(se1.getDepth(), se2.getDepth());
        if (res==0){
            if ((se1.isFolder())&&(!se2.isFolder())) res=-1;
            if ((!se1.isFolder())&&(se2.isFolder())) res=1;
        }
        if (res==0){
            res=se1.getPath().compareTo(se2.getPath());
        }
        return res;
    }
    
}
