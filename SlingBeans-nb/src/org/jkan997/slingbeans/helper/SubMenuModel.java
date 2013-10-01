/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.openide.awt.Actions;
import org.openide.util.HelpCtx;

/**
 *
 * @author jakaniew
 */
public class SubMenuModel implements Actions.SubMenuModel {

    private final List<AbstractAction> actions = new ArrayList<AbstractAction>();
    
    public void addAction(AbstractAction abstractAction){
        actions.add(abstractAction);
    }
    
    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public String getLabel(int i) {
        AbstractAction abstractAction = actions.get(i);
        return abstractAction.getActionName();
    }

    @Override
    public HelpCtx getHelpCtx(int i) {
        return null;
    }

    @Override
    public void performActionAt(int i) {
         //AbstractAction abstractAction = actions.get(i);
         
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }
}
