/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.submenu;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @author jkan997
 */
public abstract class AbstractSubmenu extends NodeAction implements Presenter.Popup {

    private String actionName;
    private Node[] nodes;

    protected AbstractSubmenu() {
        setActionName("Submenu");
        actions = new ArrayList<AbstractAction>();
    }

    protected final void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String getName() {
        return actionName;
    }
    protected List<AbstractAction> actions = null;

    public void addAction(AbstractAction abstractAction) {
        actions.add(abstractAction);
    }

    @Override
    protected boolean enable(Node[] nodes) {
        LogHelper.logInfo(this, "Enable");
        if ((nodes != null) && (nodes.length > 0)) {
            LogHelper.logInfo(this, nodes[0].toString());
        }
        this.nodes = nodes;
        return true;
    }

    protected Node getSelectedNode() {
        if ((nodes == null) || (nodes.length == 0)) {
            return null;
        }
        return nodes[0];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this = submenu => do nothing
    }

    @Override
    protected void performAction(Node[] nodes) {
        LogHelper.logInfo(this, "Perform");
        LogHelper.logInfo(this, nodes[0].toString());
    }

    protected AbstractAction[] getActionsArray() {
        return actions.toArray(new AbstractAction[]{});
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(this);
        AbstractAction[] actionsArr = getActionsArray();
        for (Action a : actionsArr) {
            menu.add(a);
        }
        return menu;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
