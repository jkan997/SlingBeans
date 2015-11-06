/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbactions.BuildBundleAction;
import org.jkan997.slingbeans.nbactions.OpenBrowserAction;
import org.jkan997.slingbeans.nbactions.OpenCrxDeAction;
import org.jkan997.slingbeans.nbactions.OpenHtmlAction;
import org.jkan997.slingbeans.nbactions.OpenLogViewerAction;
import org.jkan997.slingbeans.nbactions.ReplicateAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowAction;
import org.jkan997.slingbeans.nbactions.StartWorkflowWithDialogAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author jkan997
 */
public class CQ5Submenu extends AbstractSubmenu {

    private boolean aemMode;

    public CQ5Submenu(AbstractNode node) {
        this(node, false);
    }

    public CQ5Submenu(AbstractNode node, boolean aemMode) {
        this(node, null, aemMode);
    }

    public CQ5Submenu(AbstractNode node, FileObject fileObject, boolean aemMode) {
        setActionName(aemMode ? "AEM" : "Sling");
        actions = new ArrayList<AbstractAction>();
        if (node instanceof SlingNode) {
            SlingNode slingNode = (SlingNode) node;
            fileObject = slingNode.getFileObject();
            /*
             if (fileObject.getExt().equals("bnd")) {
             BuildBundleAction buildBundleAction = new BuildBundleAction(null);
             buildBundleAction.setSlingNode(slingNode);
             addAction(buildBundleAction);
             }*/
            if (aemMode) {
                ReplicateAction replicateAction = new ReplicateAction(slingNode);
                OpenCrxDeAction openCrxDeAction = new OpenCrxDeAction(slingNode);
                addAction(replicateAction);
                addAction(openCrxDeAction);
            }
            OpenHtmlAction openHtmlAction = new OpenHtmlAction(slingNode);
            addAction(openHtmlAction);

        }
        if (fileObject != null) {
            try {
                OpenLogViewerAction openLogAction = new OpenLogViewerAction(fileObject.getFileSystem());
                actions.add(openLogAction);
            } catch (Exception ex) {
                LogHelper.logError(ex);
            }

        }
        if (aemMode) {
            StartWorkflowAction swaAction = new StartWorkflowAction(null);
            StartWorkflowWithDialogAction swwdAction = new StartWorkflowWithDialogAction(null);
            swaAction.setFileObject(fileObject);
            swwdAction.setFileObject(fileObject);
            addAction(swaAction);
            addAction(swwdAction);
            OpenBrowserAction openWfConsoleAction = new OpenBrowserAction(fileObject);
            openWfConsoleAction.setActionName("Open Workflow console");
            openWfConsoleAction.openBrowserMode = OpenBrowserAction.OPEN_BROWSER_MODE_WF_CONSOLE;
            addAction(openWfConsoleAction);
        }

    }

    public CQ5Submenu() {
        this(null);
    }
}
