/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import javax.swing.text.StyledDocument;
import org.jkan997.slingbeans.dialogs.NewNodeDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.netbeans.api.actions.Openable;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author jkan997
 */
public class AddFileAction extends AbstractAction {

    public AddFileAction(LocalSlingNode node) {
        setActionName("Add file...");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            LocalSlingNode slingNode = (LocalSlingNode) node;
            File parentFile = new File(slingNode.getFileObject().getLocalFilePath());
            if (!parentFile.isDirectory()) {
                LogHelper.logInfo(this, "The %s is not a folder cannot add file.", parentFile.getPath());
            }
            NewNodeDialog npd = new NewNodeDialog(null, true);
            SwingHelper.showDialog(npd);
            if (npd.isCreateNode()) {
                String newFileName = npd.getSelectedNodeName();
                if ((newFileName != null) && (!"".equals(newFileName.trim()))) {

                    File newFile = new File(slingNode.getFileObject().getLocalFilePath() + "/" + newFileName);

                    FileWriter fw = new FileWriter(newFile);
                    fw.append("");
                    fw.close();
                    FileObject fo = FileUtil.toFileObject(newFile);
                    DataObject d = DataObject.find(fo);
                    System.out.println("Data object " + d);
                    EditorCookie ec = (EditorCookie) d.getCookie(EditorCookie.class);
                    if (ec != null) {
                        ec.open();
                        StyledDocument doc = ec.openDocument();
                    } else {
                        DataObject dob = DataObject.find(fo);
                        Openable oc = dob.getLookup().lookup(Openable.class);
                        if (oc != null) {
                            oc.open();
                        }
                    }
                }
                
                RefreshAction refreshAction = new RefreshAction(slingNode);
                refreshAction.actionPerformed(e);
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
