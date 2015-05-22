/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingRootNode;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.vlt.VltManager;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;

/**
 *
 * @author jkan997
 */
public abstract class VltAction extends AbstractAction {

    protected boolean exportToRemote = false;

    public VltAction(Node node) {
        this.node = node;
        LogHelper.logInfo(this, "Class %s init", this.getClass().getName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Runnable loadWorkflowsTask = new Runnable() {
            @Override
            public void run() {
                VltAction.this.runAction();
            }
        };
        ProgressUtils.runOffEventDispatchThread(loadWorkflowsTask, exportToRemote ? "Exporting to remote" : "Importing from remote", new AtomicBoolean(false), false);
    }

    private void runAction() {
        LogHelper.logInfo(this, "Action %s", this.getClass().getName());
        try {
            SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
            String fsId = slingFsFactory.getDefualtFileSystemId();
            if (fsId == null) {
                SwingHelper.showMessage("Please connect to Sling remote repository");
                return;
            }
            FileSystem fs = slingFsFactory.getFileSystem(fsId);
            LogHelper.logInfo(this, "Found fsId:%s , fs: %s", fsId, "" + fs);
            //SwingHelper.showMessage("Please connect to Sling remote repository "+fs);
            if (fs != null) {
                VltManager vltManager = fs.getVltManager();
                LocalSlingRootNode rootNode = this.getRootNode();
                String currentNodePath = null;
                LocalSlingNode currentNode = null;
                currentNode = this.getLocalSlingNode();
                boolean isRootNode = false;
                if (currentNode!=null){
                currentNodePath = currentNode.getFileObject().getFilePath();
                }
                
                if ((this.node instanceof LocalSlingRootNode)) {
                    isRootNode = true;
                }
                
                Writer outputWriter = this.getOutputWriter();
                if (!exportToRemote) {
                    if (currentNode != null) {
                        if (currentNode.getLevel() <= 1) {
                            outputWriter.write(String.format("Refusing to export first level node %s to remote server\n", currentNode.getFilePath()));
                        } else {
                            LogHelper.logInfo(this, "VltManager import %s, %s", rootNode.getContentPath(), currentNodePath);
                            vltManager.importContentToRemote(rootNode.getContentPath(), currentNodePath);
                            outputWriter.write(String.format("Exported %s to remote server %s\n", currentNode.getFilePath(), fs.toString()));
                        }
                    } else {
                        LogHelper.logInfo(this, "VltManager export current node is null.");
                    }
                } else {
                    if ((currentNode != null) && (!isRootNode)) {
                        LogHelper.logInfo(this, "VltManager export %s, %s", rootNode.getContentPath(), currentNode.getFileObject().getFilePath());
                        vltManager.exportContentFromRemote(rootNode.getContentPath(), currentNode.getFileObject().getFilePath());
                        outputWriter.write(String.format("Imported %s from remote server %s\n", currentNode.getFilePath(), fs.toString()));
                    } else {
                        String ROOT_PATH = "/";
                        LogHelper.logInfo(this, "VltManager export (root node) %s, %s", rootNode.getContentPath(), ROOT_PATH);
                        vltManager.exportContentFromRemote(rootNode.getContentPath(), ROOT_PATH);
                        outputWriter.write(String.format("Imported %s from remote server %s\n", ROOT_PATH, fs.toString()));
                    }

                }

                rootNode.refresh();
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
