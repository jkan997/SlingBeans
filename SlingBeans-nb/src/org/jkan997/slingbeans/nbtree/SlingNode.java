/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbtree;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.jkan997.slingbeans.nbactions.OpenEditorAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.AddNodeAction;
import org.jkan997.slingbeans.nbactions.AddPropertyAction;
import org.jkan997.slingbeans.nbactions.BuildBundleAction;
import org.jkan997.slingbeans.nbactions.RefreshAction;
import org.jkan997.slingbeans.nbactions.ReplicateAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowWithDialogAction;
import org.jkan997.slingbeans.nbactions.submenu.AddSubmenu;
import org.jkan997.slingbeans.nbactions.submenu.CQ5Submenu;
import org.jkan997.slingbeans.nbactions.submenu.RemoveSubmenu;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.openide.awt.Actions;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author jakaniew
 */
public class SlingNode extends AbstractNode {

    private FileObject fileObject;
    private OpenEditorAction openEditorAction = null;
    private AddPropertyAction addPropertyAction = null;
    private AddNodeAction addNodeAction = null;
    private RefreshAction refreshAction = null;
    private BuildBundleAction buildBundleAction = null;
    private ReplicateAction replicateAction = null;
    private Sheet oldSheet = null;
    private Children children;
    private Action[] actionsArr = null;
    private StartWorkflowAction startWorkflowAction;
    private StartWorkflowWithDialogAction startWorkflowWithDialogAction;
    private Actions.SubMenu startWorkflowSubmenu;

    public SlingNode(Children children) {
        super(children);
        this.children = children;
    }

    public SlingNode(Children children, Lookup lookup) {
        super(children, lookup);
        this.children = children;
        openEditorAction = new OpenEditorAction(this);
        refreshAction = new RefreshAction(this);
    }

    @Override
    public Action[] getActions(boolean popup) {
        if (actionsArr == null) {
            List<Action> actions = new ArrayList<Action>();
            if (fileObject.isSlingFile()) {
                actions.add(openEditorAction);
            }
            actions.add(addNodeAction);
            actions.add(addPropertyAction);
            boolean isCQ5 = false;
            try {
                isCQ5 = this.getFileObject().getFileSystem().isCQ5();
            } catch (FileStateInvalidException ex) {
                LogHelper.logError(ex);
            }
            if (isCQ5) {
                AddSubmenu addSubmenu = new AddSubmenu(this);
                actions.add(addSubmenu);
                RemoveSubmenu removeSubmenu = new RemoveSubmenu(this);
                actions.add(removeSubmenu);
                
                CQ5Submenu cq5Submenu = new CQ5Submenu(this);
                actions.add(cq5Submenu);
                RefreshAction refreshAction = new RefreshAction(this);
                actions.add(refreshAction);
            }

            actionsArr = actions.toArray(new Action[]{});
        }
        return actionsArr;
    }

    @Override
    public Action getPreferredAction() {
        return openEditorAction;
    }

    public void addFakeChildren() {
        /*        SlingNode sn = new SlingNode(null);
         sn.se
         this.children.add(new Node[]{sn});*/
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set props = sheet.get(Sheet.PROPERTIES);
        for (String key : fileObject.getAttributesMap().keySet()) {
            FileObjectAttribute foa = fileObject.getAttribute(key);
            if (!foa.isHidden()) {
                SlingNodeProperty prop = new SlingNodeProperty(foa.getTypeClass());
                prop.setName(key);
                prop.setFileObject(fileObject);
                prop.setAttrName(key);

                props.put(prop);
            }
        }

        SlingNodeProperty createdProp = new SlingNodeProperty(Date.class);
        createdProp.setName("Created");
        createdProp.setSpecialAttr(SlingNodeProperty.ATTR_CREATED, fileObject.getCreated());
        props.put(createdProp);

        if (fileObject.isFile()) {
            SlingNodeProperty lastModifiedProp = new SlingNodeProperty(Date.class);
            lastModifiedProp.setName("Created");
            lastModifiedProp.setSpecialAttr(SlingNodeProperty.ATTR_LAST_MODIFIED, fileObject.lastModified());
            props.put(lastModifiedProp);

            SlingNodeProperty sizeProp = new SlingNodeProperty(Long.class);
            sizeProp.setName("Size");
            sizeProp.setSpecialAttr(SlingNodeProperty.ATTR_SIZE, fileObject.getSize());
            props.put(sizeProp);

        }
        oldSheet = sheet;
        return sheet;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    public String getPath() {
        if (fileObject != null) {
            return fileObject.getPath();
        }
        return null;
    }

    public void refresh() {
        refresh(true);
    }

    public void refresh(boolean expandNodeAfter) {
        Node parentNode = this.getParentNode();
        Children children = parentNode.getChildren();
        if (children instanceof SlingNodeChildren) {
            SlingNodeChildren snc = (SlingNodeChildren) children;
            snc.reload();
            if (expandNodeAfter) {
                getRootNode().getBeanTreeView().expandNode(this);
            }
        }

    }

    public void setProperty(String name, Object value) {
        try {
            FileObject fo = getFileObject();
            fo.setAttribute(name, value);
            fo.saveAttributes();
            //this.firePropertySetsChange(new PropertySet[]{oldProps},new PropertySet[]{newProps});
            this.firePropertySetsChange(null, this.getPropertySets());
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    @Override
    public Image getIcon(int type) {
        if (fileObject.isSlingFolder()) {
            return Utilities.loadImage("org/jkan997/slingbeans/nbicons/folder.png");
        } else if (fileObject.isSlingFile()) {
            return Utilities.loadImage("org/jkan997/slingbeans/nbicons/file.png");
        } else {
            return Utilities.loadImage("org/jkan997/slingbeans/nbicons/node.png");
        }
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public PasteType getDropType(final Transferable t, int arg1, int arg2) {
        final Node node = NodeTransfer.node(t, arg1);
        return new PasteType() {
            @Override
            public Transferable paste() throws IOException {
                String msg = ((SlingNode) node).getDisplayName();
                SwingHelper.showMessage(msg);
                return null;
            }
        };
    }

    public SlingRootNode getRootNode() {
        Node node = this;
        do {
            if (node instanceof SlingRootNode) {
                return (SlingRootNode) node;
            }
            node = node.getParentNode();
        } while (node != null);
        return null;
    }
}
