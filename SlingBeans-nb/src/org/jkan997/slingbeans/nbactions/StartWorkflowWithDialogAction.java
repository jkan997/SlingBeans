/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions;

import static javax.swing.Action.NAME;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;


public class StartWorkflowWithDialogAction extends StartWorkflowAction {

    public StartWorkflowWithDialogAction(DataObject context) {
        super(context);
        setActionName( "Start workflow...");
        this.alwaysShowDialog = true;
    }
}
