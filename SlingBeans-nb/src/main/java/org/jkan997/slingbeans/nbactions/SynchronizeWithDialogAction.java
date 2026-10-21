/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import org.openide.loaders.DataObject;

public class SynchronizeWithDialogAction extends SynchronizeAction {

    public SynchronizeWithDialogAction(DataObject context) {
        super(context);
        this.setActionName("Synchronize...");
        this.alwaysShowDialog = true;
    }
}
