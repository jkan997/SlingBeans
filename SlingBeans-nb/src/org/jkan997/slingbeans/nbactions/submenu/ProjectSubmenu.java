package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbactions.BuildBundleAction;
import org.jkan997.slingbeans.nbactions.CheckoutAction;
import org.jkan997.slingbeans.nbactions.SynchronizeAction;
import org.jkan997.slingbeans.nbactions.SynchronizeWithDialogAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.submenu.ProjectSubmenu")
@ActionRegistration(
        displayName = "Sling sm")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 111)
})
public class ProjectSubmenu extends AbstractSubmenu implements Presenter.Popup {

    protected final DataObject context;

    private ProjectSubmenu() {
        actions = new ArrayList<AbstractAction>();
        context = null;
        this.setActionName("SlingBeans");

    }

    @Override
    protected boolean enable(Node[] nodes) {
        super.enable(nodes);
        Node node = this.getSelectedNode();
        DataObject dataObject = node.getCookie(DataObject.class);
        if (dataObject == null) {
            LogHelper.logInfo(this, "Null data object");
            return false;
        }
        LogHelper.logInfo(this, "Submenu", dataObject);
        actions.clear();
        CheckoutAction checkoutAction = new CheckoutAction(dataObject);
        addAction(checkoutAction);
        SynchronizeAction synchronizeAction = new SynchronizeAction(dataObject);
        addAction(synchronizeAction);
        SynchronizeWithDialogAction synchronizeWithDialogAction = new SynchronizeWithDialogAction(dataObject);
        addAction(synchronizeWithDialogAction);
        BuildBundleAction buildBundleAction = new BuildBundleAction(dataObject);
        addAction(buildBundleAction);
        return true;
    }
}
