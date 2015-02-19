/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jkan997.slingbeans.nbprojects.maven.actions;

import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;

/**
 *
 * @author jakaniew
 */
public class VltExportAction extends VltAction{

    public VltExportAction(LocalSlingNode node) {
        super(node);
        this.setActionName("Export to server");
        this.exportToRemote=false;
    }
}
