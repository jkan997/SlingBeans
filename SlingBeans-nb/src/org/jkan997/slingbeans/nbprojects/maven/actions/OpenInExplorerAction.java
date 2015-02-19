/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbtree.SlingTreeTopComponent;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

/**
 *
 * @author jakaniew
 */
public class OpenInExplorerAction extends AbstractAction {

    public OpenInExplorerAction(Node node) {
        this.node = node;
        this.setActionName("Open in Sling Explorer");
        LogHelper.logInfo(this, "Class %s init", this.getClass().getName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LocalSlingNode currentNode = this.getLocalSlingNode();
        String nodePath = currentNode.getFileObject().getFilePath();
        LogHelper.logInfo(this, "Node path %s", nodePath);

        //  Lookup.Result<SlingTreeTopComponent> lookupResult = Utilities.actionsGlobalContext().lookupResult(org.jkan997.slingbeans.nbtree.SlingTreeTopComponent.class);
        SlingTreeTopComponent tc = (SlingTreeTopComponent) WindowManager.getDefault().findTopComponent("SlingTreeTopComponent");
        if (tc != null) {
            LogHelper.logInfo(this, "lookupResult %s", tc);
            tc.requestActive();
            tc.gotoPath(nodePath);
        }

    }

}
