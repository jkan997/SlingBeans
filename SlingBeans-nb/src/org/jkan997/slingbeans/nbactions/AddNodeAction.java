/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.dialogs.NewNodeDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;

public class AddNodeAction extends AbstractAction {

    private SlingNode node;
    private String initialSelection = null;
    private boolean lockSelection = false;

    public AddNodeAction(SlingNode node) {
        setActionName("Add node...");
        this.node = node;
    }

    public boolean isLockSelection() {
        return lockSelection;
    }

    public void setLockSelection(boolean lockSelection) {
        this.lockSelection = lockSelection;
    }

  

    public String getInitialSelection() {
        return initialSelection;
    }

    public void setInitialSelection(String initialSelection) {
        this.initialSelection = initialSelection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileSystem fs;
        try {
            fs = node.getFileObject().getFileSystem();

            NewNodeDialog npd = new NewNodeDialog(null, true);

            if (initialSelection != null) {
                npd.setInitialSelection(initialSelection);
            }

            npd.setNodeTypes(fs.getNodeTypes());
            SwingHelper.showDialog(npd);
            if (npd.isCreateNode()) {
                FileObject parentFo = node.getFileObject();
                String selectedNodeType = npd.getSelectedNodeType();
                String selectedNodeName = npd.getSelectedNodeName();
                if (selectedNodeType.equals(NodeTypeSet.NT_FILE)){
                    fs.createFile("/"+parentFo.getPath()+"/"+selectedNodeName,"alfa!");
                } else {
                    parentFo.createNode(selectedNodeName, selectedNodeType);
                }
                fs.commmit();
                node.refresh(false);
            }

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }
}
