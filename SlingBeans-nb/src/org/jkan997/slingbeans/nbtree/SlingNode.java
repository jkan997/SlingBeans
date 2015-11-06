/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbtree;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.jkan997.slingbeans.nbactions.OpenEditorAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.ObjectHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.node.AddNodeAction;
import org.jkan997.slingbeans.nbactions.property.AddPropertyAction;
import org.jkan997.slingbeans.nbactions.BuildBundleAction;
import org.jkan997.slingbeans.nbactions.node.RefreshAction;
import org.jkan997.slingbeans.nbactions.ReplicateAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowWithDialogAction;
import org.jkan997.slingbeans.nbactions.clipboard.CopyNodeAction;
import org.jkan997.slingbeans.nbactions.clipboard.CutNodeAction;
import org.jkan997.slingbeans.nbactions.clipboard.PasteNodeAction;
import org.jkan997.slingbeans.nbactions.node.RemoveNodeAction;
import org.jkan997.slingbeans.nbactions.property.RemovePropertyAction;
import org.jkan997.slingbeans.nbactions.submenu.AddSubmenu;
import org.jkan997.slingbeans.nbactions.submenu.CQ5Submenu;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.openide.awt.Actions;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author jkan997
 */
public class SlingNode extends AbstractNode {

    private FileObject fileObject;
    private OpenEditorAction openEditorAction = null;
    private AddPropertyAction addPropertyAction = null;
    private AddNodeAction addNodeAction = null;
    private RefreshAction refreshAction = null;
    private BuildBundleAction buildBundleAction = null;
    private ReplicateAction replicateAction = null;
    private CopyNodeAction copyNodeAction;
    private CutNodeAction cutNodeAction;
    private PasteNodeAction pasteNodeAction;
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
        copyNodeAction = new CopyNodeAction(this);
        cutNodeAction = new CutNodeAction(this);
        pasteNodeAction = new PasteNodeAction(this);
    }

    @Override
    public Action[] getActions(boolean popup) {
        if (actionsArr == null) {
            List<Action> actions = new ArrayList<Action>();
            if (fileObject.isSlingFile()) {
                actions.add(openEditorAction);
            }
            try {
                boolean aemMode = this.getFileObject().getFileSystem().isCQ5();
                CQ5Submenu cq5Submenu = new CQ5Submenu(this, aemMode);
                actions.add(cq5Submenu);
            } catch (FileStateInvalidException ex) {
                LogHelper.logError(ex);
            }
            actions.add(addNodeAction);
            actions.add(copyNodeAction);
            actions.add(cutNodeAction);
            actions.add(pasteNodeAction);
            actions.add(addPropertyAction);

            AddSubmenu addSubmenu = new AddSubmenu(this);
            actions.add(addSubmenu);
            /*RemoveSubmenu removeSubmenu = new RemoveSubmenu(this);
             actions.add(removeSubmenu);*/

            RemoveNodeAction removeNodeAction = new RemoveNodeAction(this);
            actions.add(removeNodeAction);

            RefreshAction refreshAction = new RefreshAction(this);
            actions.add(refreshAction);

            AddPropertyAction addPropertyAction = new AddPropertyAction(this);
            actions.add(addPropertyAction);

            RemovePropertyAction removePropertyAction = new RemovePropertyAction(this);
            actions.add(removePropertyAction);

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

    private SlingNodeProperty createSlingNodeProperty(String key, FileObjectAttribute foa) {
        SlingNodeProperty prop = new SlingNodeProperty(foa.getTypeClass());
        prop.setName(key);
        prop.setFileObject(fileObject);
        prop.setAttrName(key);
        return prop;
    }

    private void updateUpdatePropertySheet(Sheet.Set props) {
        if (props == null) {
            return;
        }
        List<String> attrNames = Collections.list(props.attributeNames());
        for (String attrName : attrNames) {
            props.remove(attrName);
        }
        Map<String, FileObjectAttribute> attrsMap = fileObject.getAttributesMap();
        for (String key : attrsMap.keySet()) {
            FileObjectAttribute foa = attrsMap.get(key);
            if (!foa.isHidden()) {
                SlingNodeProperty prop = createSlingNodeProperty(key, foa);
                //   prop.
                props.put(prop);

            }
        }
        SlingNodeProperty createdProp = new SlingNodeProperty(Date.class
        );
        createdProp.setName(
                "Created");
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
    }

    /*
    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set props = sheet.get(Sheet.PROPERTIES);
        updateUpdatePropertySheet(props);
        oldSheet = sheet;
        return sheet;
    }*/

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fo) {
        this.fileObject = fo;
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
        Children children = this.getChildren();
        FileObject fo = this.getFileObject();
        fo.setChildrenLoaded(false);
        Map<String, FileObjectAttribute> oldProps = ObjectHelper.cloneMap(fo.getAttributesMap());
        fo.refresh();
        Map<String, FileObjectAttribute> newProps = ObjectHelper.cloneMap(fo.getAttributesMap());
        Set<String> keys = new TreeSet<String>();
        keys.addAll(oldProps.keySet());
        keys.addAll(newProps.keySet());
        for (String key : keys) {
            FileObjectAttribute oldVal = oldProps.get(key);
            FileObjectAttribute newVal = newProps.get(key);
            LogHelper.logInfo(this, "%s = %s [%s]", key, oldVal, newVal);

            if (!ObjectHelper.equalObjects(oldVal, newVal)) {
                this.firePropertyChange(key, oldVal, newVal);
                LogHelper.logInfo(this, "firePropertyChange(%s, %s, %s)", key, oldVal, newVal);
            }
        }
        if (oldSheet != null) {
            Sheet.Set props = oldSheet.get(Sheet.PROPERTIES);
            updateUpdatePropertySheet(props);
        }
        if (children instanceof SlingNodeChildren) {
            if (fo.isLeafNode()) {
                this.setChildren(Children.LEAF);
            } else {
                SlingNodeChildren snc = (SlingNodeChildren) children;
                snc.reload();
            }

        } else {
            if (!fo.isLeafNode()) {
                SlingNodeChildren snc = new SlingNodeChildren(fo);
                this.setChildren(snc);
            }

        }
        if (expandNodeAfter) {
            /*
             getRootNode().getBeanTreeView().expandNode(this);*/
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
