/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.text.StyledDocument;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.netbeans.api.actions.Openable;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author jkan997
 */
public class OpenEditorAction extends AbstractAction {

    public OpenEditorAction(LocalSlingNode node) {
        setActionName("Open in editor...");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            LocalSlingNode slingNode = (LocalSlingNode) node;
            FileObject fo = FileUtil.toFileObject(new File(slingNode.getFileObject().getLocalFilePath()));
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
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
