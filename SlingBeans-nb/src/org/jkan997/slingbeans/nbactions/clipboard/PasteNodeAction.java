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
import org.openide.filesystems.FileStateInvalidException;

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
                final Runnable pasteNodeAction = new Runnable() {
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
                           // node.getP
                        } catch (Exception ex) {
                            LogHelper.logError(ex);
                        }
                    }
                };
                ProgressUtils.runOffEventDispatchThread(pasteNodeAction, "Replicating node", new AtomicBoolean(false), false);
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }

}
