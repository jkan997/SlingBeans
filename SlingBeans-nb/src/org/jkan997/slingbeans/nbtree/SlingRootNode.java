/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbtree;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.OpenLogViewerAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowWithDialogAction;
import org.jkan997.slingbeans.nbactions.submenu.CQ5Submenu;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author jakaniew
 */
public class SlingRootNode extends AbstractNode {

    private FileObject rootFileObject = null;
    private BeanTreeView beanTreeView;
    private StartWorkflowAction startWorkflowAction = null;
    private Action[] actionsArr = null;
    private OpenLogViewerAction openLogAction;
    private StartWorkflowWithDialogAction startWorkflowWithDialogAction;

    public SlingRootNode(Children children) {
        super(children);
    }

    public SlingRootNode(Children children, Lookup lookup) {
        super(children, lookup);
    }

    @Override
    public Action[] getActions(boolean popup) {

        if (actionsArr == null) {
            List<Action> actions = new ArrayList<Action>();
            try {
                if (rootFileObject.getFileSystem().isCQ5()) {
                    CQ5Submenu cq5submenu = new CQ5Submenu(this, this.rootFileObject);
                    actions.add(cq5submenu);
                }
            } catch (Exception ex) {
                LogHelper.logError(ex);
            }
            actionsArr = actions.toArray(new Action[]{});
        }
        return actionsArr;
    }

    @Override
    public String getName() {
        return "Sling Repository";
    }

    @Override
    public Image getIcon(int type) {
        return Utilities.loadImage("org/jkan997/slingbeans/nbicons/folder.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    public BeanTreeView getBeanTreeView() {
        return beanTreeView;
    }

    public void setBeanTreeView(BeanTreeView beanTreeView) {
        this.beanTreeView = beanTreeView;
    }

    public FileObject getRootFileObject() {
        return rootFileObject;
    }

    public void setRootFileObject(FileObject rootFileObject) {
        this.rootFileObject = rootFileObject;
    }
    
     public void refresh() {
        refresh(true);
    }

    public void refresh(boolean expandNodeAfter) {
      
    }

}
