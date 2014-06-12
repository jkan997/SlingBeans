/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.VLTHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;

public class VLTAction extends AbstractAction {

    /*public final static int VLT_EXPORT = 1;
     public final static int VLT_IMPORT = 2;
     public final static int OPEN_BROWSER_MODE_WF_CONSOLE = 3;
     public int openBrowserMode = OPEN_BROWSER_MODE_CRXDE;*/
    private final FileObject fileObject;

    public VLTAction(SlingNode node) {
        this(node != null ? node.getFileObject() : null);
    }

    public VLTAction(FileObject fileObject) {
        this.setActionName("VLT export");
        this.fileObject = fileObject;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileSystem fs = fileObject.getFileSystem();
            String BOSE = "/Users/jakaniew/svn/Adobe/AdobeCustomDemos/BOSE/git/content/src/main/content/";
            String vltOutput = VLTHelper.export(fs, "/" + fileObject.getPath(), BOSE);
            this.getOutputWriter().append(vltOutput);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
