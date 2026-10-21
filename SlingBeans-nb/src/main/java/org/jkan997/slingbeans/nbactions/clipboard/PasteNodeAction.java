/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.clipboard;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;

class PasteNodeActionRunnable implements Runnable {

    private final FileSystem fs;
    private final SlingNode node;
    private final String cliboardSrc;

    public PasteNodeActionRunnable(FileSystem fs, SlingNode node, String cliboardSrc) {
        this.fs = fs;
        this.node = node;
        this.cliboardSrc = cliboardSrc;
    }

    private void findAndRefresh(Node node, String path, boolean ignoreLast) {
        String[] pathArr = path.split("/");
        boolean childFound = false;
        StringBuilder pathSb = new StringBuilder();
        for (int i = 0; i < pathArr.length - (ignoreLast ? 1 : 0); i++) {
            String part = pathArr[i];
            for (Node child : node.getChildren().getNodes()) {
                String nodeName = child.getDisplayName();
                if (nodeName.equals(part)) {
                    childFound = true;
                    node = child;
                    pathSb.append("/" + node.getName());
                    break;
                }
            }
            if (!childFound) {
                break;
            }
        }
        if (node instanceof SlingNode) {
            LogHelper.logInfo(this, "Refreshing path %s", pathSb);
            ((SlingNode) node).refresh();
        }
    }

    @Override
    public void run() {
        try {
            boolean copy = fs.isClipboardCopy();
            String dest = node.getPath();
            if (copy) {
                fs.copyNode(cliboardSrc, dest);
            } else {
                fs.moveNode(cliboardSrc, dest);
            }
            node.refresh();
            findAndRefresh(node.getRootNode(), cliboardSrc, true);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}

@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.clipboard.PasteAction")
@ActionRegistration(asynchronous = true, displayName = "Paste node")
public class PasteNodeAction extends AbstractAction {

    private SlingNode node;

    public PasteNodeAction(SlingNode node) {
        setActionName("Paste node");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            final FileSystem fs = (FileSystem) node.getFileObject().getFileSystem();
            final String cliboardSrc = fs.getClipboardContent();
            if ((this.node != null) && (cliboardSrc != null)) {
                final Runnable pasteNodeAction = new PasteNodeActionRunnable(fs, node, cliboardSrc);
                ProgressUtils.runOffEventDispatchThread(pasteNodeAction, "Replicating node", new AtomicBoolean(false), false);
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }

}
