package org.jkan997.slingbeans.nbprojects.maven;

/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.submenu.CQ5Submenu;
import org.jkan997.slingbeans.nbprojects.maven.actions.AddFileAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.OpenEditorAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.OpenInExplorerAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.RefreshAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.VltExportAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.VltImportAction;
import org.jkan997.slingbeans.slingfs.local.LocalFileObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author jkan997
 */
public class LocalSlingNode extends LocalAbstractNode {

    private LocalFileObject fileObject;
    private Children children;
    private Action[] actionArr = null;
    private OpenEditorAction openEditorAction;
    private RefreshAction refreshAction;
    private VltImportAction vltImportAction;
    private VltExportAction vltExportAction;
    private OpenInExplorerAction openInExplorerAction;
    private CQ5Submenu cq5submenu;
    private AddFileAction addFileAction;

    public LocalSlingNode(LocalFileObject fileObject, Children children) {
        super(children, Lookups.singleton(fileObject));
        this.children = children;
        this.fileObject = fileObject;
        initActions();
    }

    private void initActions() {
        openEditorAction = new OpenEditorAction(this);
        openInExplorerAction = new OpenInExplorerAction(this);
        addFileAction = new AddFileAction(this);
        refreshAction = new RefreshAction(this);
        vltImportAction = new VltImportAction(this);
        vltExportAction = new VltExportAction(this);
        //cq5submenu = new CQ5Submenu(this);
        //openXmlDescriptorAction = new OpenXmlDescriptorAction(this);
        /*AddSubmenu addSubmenu = new AddSubmenu(this);
         actions.add(addSubmenu);*/

    }

    @Override
    public Action[] getActions(boolean popup) {
        if (actionArr == null) {
            List<Action> actions = new ArrayList<Action>();
            if (fileObject.isSlingFile()) {
                actions.add(openEditorAction);
            }
            actions.add(openInExplorerAction);

            //actions.add(cq5submenu);
            actions.add(refreshAction);
            actions.add(vltImportAction);
            actions.add(vltExportAction);
            if (fileObject.isSlingFolder()) {
                actions.add(addFileAction);

            }
            actionArr = actions.toArray(new Action[]{});
        }
        return actionArr;
    }

    @Override
    public Action getPreferredAction() {
        if (fileObject.isSlingFile()) {
            return openEditorAction;
        }
        return null;
    }

    public LocalFileObject getFileObject() {
        return fileObject;
    }

    public int getLevel() {
        int level = 0;
        String filePath = fileObject.getFilePath();
        char[] chars = filePath.toCharArray();
        for (char c : chars) {
            if (c == '/') {
                level++;
            }
        }
        LogHelper.logInfo(this, filePath + " LEVEL: " + level);
        return level;
    }

    public void refresh() {
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
        /*final Node node = NodeTransfer.node(t, arg1);
         return new PasteType() {
         @Override
         public Transferable paste() throws IOException {
         String msg = ((SlingNode) node).getDisplayName();
         SwingHelper.showMessage(msg);
         return null;
         }
         };*/
        return null;
    }

    public LocalSlingRootNode getRootNode() {
        Node node = this;
        do {
            if (node instanceof LocalSlingRootNode) {
                return (LocalSlingRootNode) node;
            }
            node = node.getParentNode();
        } while (node != null);
        return null;
    }

    public String getFilePath() {
        return fileObject.getFilePath();
    }

    @Override
    public String toString() {
        return this.getDisplayName();
    }

}
