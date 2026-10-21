/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.TreeSet;
import org.jkan997.slingbeans.dialogs.OpenLogViewerDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nblogviewer.LogViewer;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.util.Exceptions;

public class OpenLogViewerAction extends AbstractAction {

    private final FileSystem fs;
    private Set<String> logFiles = new TreeSet<String>();
    private final SlingNode node;

    public OpenLogViewerAction(FileSystem fs) {
        setActionName("Open log");
        this.fs = fs;
        this.node = null;
    }

    public OpenLogViewerAction(SlingNode node) {
        setActionName("Open log");
        this.node = node;
        this.fs = null;
    }

    private void openLogFile(String logName) {
        LogViewer.openLogViewer(fs, logName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OpenLogViewerDialog olvd = new OpenLogViewerDialog(null, true);
        FileSystem fs = getFileSystem();
        olvd.init(fs);
        SwingHelper.showDialog(olvd);
        System.out.println(olvd.isLogFilesSelected());
        Set<String> logFiles = olvd.getSelectedLogFiles();
        LogViewer.closeAllLogViewers();
        for (String logFile : logFiles) {
            openLogFile(logFile);
        }

    }

    private FileSystem getFileSystem() {
        if (this.fs != null) {
            return this.fs;
        }
        SlingNode slingNode = null;
        if (this.node != null) {
            slingNode = this.node;
        }
        if (this.localNode != null) {
           // slingNode = getSlingNode();
        }
        if (slingNode != null) {
            FileObject fileObject = slingNode.getFileObject();
            try {
                return fileObject.getFileSystem();
            } catch (FileStateInvalidException ex) {
                LogHelper.logError(ex);
            }
        }
        return null;

    }

}
