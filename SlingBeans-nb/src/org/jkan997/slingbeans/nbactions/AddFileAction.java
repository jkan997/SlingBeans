package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.util.Map;
import org.jkan997.slingbeans.dialogs.NewNodeDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.filesystems.FileStateInvalidException;

public class AddFileAction extends AbstractAction {

    private SlingNode node;

    public AddFileAction(SlingNode node) {
        setActionName("Add file");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileSystem fs;
        try {
            fs = node.getFileObject().getFileSystem();
            FileObject parentFo = node.getFileObject();

            node.refresh();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }
}
