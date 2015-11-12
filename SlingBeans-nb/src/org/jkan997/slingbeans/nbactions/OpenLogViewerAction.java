/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jkan997.slingbeans.dialogs.OpenLogViewerDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nblogviewer.LogViewer;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;

public class OpenLogViewerAction extends AbstractAction {

    private final FileSystem fs;
    private Set<String> logFiles = new TreeSet<String>();

    public OpenLogViewerAction(FileSystem fs) {
        setActionName("Open log");
        this.fs = fs;
    }

   
    
    private void openLogFile(String logName){
        LogViewer.openLogViewer(fs, logName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         OpenLogViewerDialog olvd = new OpenLogViewerDialog(null, true);
        olvd.init(fs);
        SwingHelper.showDialog(olvd);
        System.out.println(olvd.isLogFilesSelected());
        Set<String> logFiles = olvd.getSelectedLogFiles();
        LogViewer.closeAllLogViewers();
        for (String logFile : logFiles) {
            openLogFile(logFile);
        }
        
    }
}
