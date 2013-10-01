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

    public void loadLogFiles() {
        final Runnable initSlingFsTask = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "/bin/remotelog.html";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "list");
                    byte[] resp = fs.sendGet(url, params);
                    ByteArrayInputStream is = new ByteArrayInputStream(resp);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    boolean firstLine = true;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (!firstLine) {
                            logFiles.add(line);
                        } else {
                            firstLine = false;
                        }
                    }

                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(initSlingFsTask, "Loading log file list", new AtomicBoolean(false), false);

    }
    
    private void openLogFile(String logName){
        LogViewer.openLogViewer(fs, logName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        loadLogFiles();
         OpenLogViewerDialog olvd = new OpenLogViewerDialog(null, true);
        olvd.init(logFiles);
        SwingHelper.showDialog(olvd);
        System.out.println(olvd.isLogFilesSelected());
        Set<String> logFiles = olvd.getSelectedLogFiles();
        LogViewer.closeAllLogViewers();
        for (String logFile : logFiles) {
            openLogFile(logFile);
        }
        
    }
}
