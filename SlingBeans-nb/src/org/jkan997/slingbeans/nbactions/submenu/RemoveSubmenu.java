/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;

/**
 *
 * @author jakaniew
 */
public class RemoveSubmenu extends AbstractSubmenu {

    public RemoveSubmenu(SlingNode node) {
        setActionName("Remove");
        actions = new ArrayList<AbstractAction>();
        
        if (node instanceof SlingNode) {
        //    addAction(replicateAction);
        }

    }

    public RemoveSubmenu() {
        this(null);
    }
}
