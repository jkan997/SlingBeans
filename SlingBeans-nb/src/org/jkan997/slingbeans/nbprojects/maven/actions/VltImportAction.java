/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jkan997.slingbeans.nbprojects.maven.actions;

import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public class VltImportAction extends VltAction{

    public VltImportAction(Node node) {
        super(node);
        this.setActionName("Import from server");
        this.importFromRemote=true;

    }
}
