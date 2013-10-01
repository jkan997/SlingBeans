/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
