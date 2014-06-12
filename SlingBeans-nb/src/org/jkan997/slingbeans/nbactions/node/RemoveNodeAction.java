/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.node;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;

public class RemoveNodeAction extends AbstractAction {

    private SlingNode node;
    private String initialSelection = null;
    private boolean lockSelection = false;

    public RemoveNodeAction(SlingNode node) {
        setActionName("Remove node...");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            FileObject fo = node.getFileObject();
            String msg = String.format("Are you sure to delete file: %s", fo.getPath());
            boolean confirmed = SwingHelper.showConfirmation(msg);
            if (confirmed) {
                FileSystem fs = fo.getFileSystem();
                fs.remove(fo.getPath());
                SlingNode parentNode = (SlingNode)node.getParentNode();
                parentNode.refresh();
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }
}
