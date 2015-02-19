/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public class OpenXmlDescriptorAction extends AbstractAction {

    public OpenXmlDescriptorAction(Node node) {
        this.node = node;
        LogHelper.logInfo(this, "Class %s init", this.getClass().getName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
