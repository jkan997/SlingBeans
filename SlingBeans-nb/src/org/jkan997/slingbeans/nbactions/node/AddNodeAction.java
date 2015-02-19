/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.node;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.jkan997.slingbeans.dialogs.NewNodeDialog;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.openide.nodes.AbstractNode;

public class AddNodeAction extends AbstractAction {

    private SlingNode slingNode;
    private LocalSlingNode localSlingNode;
    private String initialSelection = null;
    private boolean lockSelection = false;

    public AddNodeAction(SlingNode node) {
        setActionName("Add node...");
        this.slingNode = node;
    }

    public AddNodeAction(AbstractNode node) {
        setActionName("Add node...");
        if (node instanceof SlingNode) {
            slingNode = (SlingNode) node;
        }
        if (node instanceof LocalSlingNode) {
            localSlingNode = (LocalSlingNode) node;
        }
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

            NewNodeDialog npd = new NewNodeDialog(null, true);

            if (initialSelection != null) {
                npd.setInitialSelection(initialSelection);
            }

            npd.setNodeTypes(getNodeTypes());
            SwingHelper.showDialog(npd);
            if (npd.isCreateNode()) {
                String selectedNodeType = npd.getSelectedNodeType();
                String selectedNodeName = npd.getSelectedNodeName();
                File selectedFile = npd.getSelectedFile();
                byte[] fileContent = null;
                if ((selectedFile!=null)&&(selectedFile.exists())){
                    fileContent = IOHelper.readFileToBytes(selectedFile);
                }
                createNode(selectedNodeName, selectedNodeType,fileContent);
            }

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }

    private void createNode(String selectedNodeName, String selectedNodeType, byte[] content) throws IOException {
        if (slingNode != null) {
            FileSystem fs = slingNode.getFileObject().getFileSystem();
            FileObject parentFo = slingNode.getFileObject();
            if (selectedNodeType.equals(NodeTypeSet.NT_FILE)) {
                String newNodePath = "/" + parentFo.getPath() + "/" + selectedNodeName;
                if (content!=null){
                    fs.createFile(newNodePath, content);
                } else {
                    fs.createFile(newNodePath, "");
                }
            } else {
                parentFo.createNode(selectedNodeName, selectedNodeType);
            }
            fs.commmit();
            slingNode.refresh(false);
        }

    }

    private Set getNodeTypes() {
        if (slingNode != null) {
            try{
            FileSystem fs = slingNode.getFileObject().getFileSystem();
            return fs.getNodeTypes();
            } catch (Exception ex){
                LogHelper.logError(ex);
            }
        }
        return Collections.EMPTY_SET;
    }
}
