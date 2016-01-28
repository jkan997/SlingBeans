/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.awt.HtmlBrowser.URLDisplayer;

public class OpenBrowserAction extends AbstractAction {

    public final static int OPEN_BROWSER_MODE_HTML = 1;
    public final static int OPEN_BROWSER_MODE_CRXDE = 2;
    public final static int OPEN_BROWSER_MODE_WF_CONSOLE = 3;
    public final static int OPEN_BROWSER_MODE_SYSTEM_CONSOLE = 4;

    public int openBrowserMode = OPEN_BROWSER_MODE_CRXDE;
    private final FileObject fileObject;

    public OpenBrowserAction(SlingNode node) {
        if (node != null) {
            fileObject = node.getFileObject();
        } else {
            fileObject = null;
        }
    }

    public OpenBrowserAction(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //URLDisplayer urlDisplayer = URLDisplayer.getDefault();
            StringBuilder urlSb = new StringBuilder(fileObject.getFileSystem().getServerPrefix());
            if (openBrowserMode == OPEN_BROWSER_MODE_CRXDE) {
                urlSb.append("/crx/de/index.jsp#/" + fileObject.getPath());
            } else if (openBrowserMode == OPEN_BROWSER_MODE_WF_CONSOLE) {
                urlSb.append("/workflow");
            }else if (openBrowserMode == OPEN_BROWSER_MODE_SYSTEM_CONSOLE) {
                urlSb.append("/system/console/configMgr");
            }  else {
                urlSb.append("/" + fileObject.getPath() + ".html");
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(urlSb.toString()));
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
