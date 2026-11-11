/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.clipboard;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.util.Exceptions;

@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.clipboard.CutAction")
@ActionRegistration(asynchronous = true, displayName = "Cut node")
public class CutNodeAction extends AbstractAction {

    private SlingNode node;

    public CutNodeAction(SlingNode node) {
        setActionName("Cut node");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.node != null) {
            try {
                FileSystem fs = (FileSystem) this.node.getFileObject().getFileSystem();
                fs.setClipboardContent(this.node.getPath(), false);
            } catch (FileStateInvalidException ex) {
                LogHelper.logError(ex);
            }
        }

    }

}
