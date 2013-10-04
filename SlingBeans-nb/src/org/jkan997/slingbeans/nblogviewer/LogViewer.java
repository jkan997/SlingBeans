/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nblogviewer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.jkan997.slingbeans.helper.DisposableTimerTask;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 *
 * @author jkan997
 */
public class LogViewer {

    private InputOutput io;
    private Timer timer;
    private long lastPos = 0;
    private String logName = null;
    private OutputWriter logWriter;
    private FileSystem fs;
    private Map<String, String> params = new HashMap<String, String>();
    private final static Set<LogViewer> logViewers = new HashSet<LogViewer>();

    public synchronized static void openLogViewer(FileSystem fs, String logName) {
        IOProvider iop = IOProvider.getDefault();
        String ioName = logName;
        InputOutput io = iop.getIO(ioName, false);
        if (io != null) {
            io.closeInputOutput();
        }
        io = iop.getIO(ioName, false);
        io.setOutputVisible(true);
        io.select();
        closeLogViewer(logName);
        LogViewer lv = new LogViewer();
        lv.init(io, fs, logName);
        logViewers.add(lv);
    }

    public synchronized static void closeLogViewer(String logName) {
        for (LogViewer lv : logViewers) {
            if (lv.getLogName().equals(logName)) {
                lv.dispose();
            }
        }
    }

    public synchronized static void closeAllLogViewers() {
        for (LogViewer lv : logViewers) {
            lv.dispose();
        }

    }

    public String getLogName() {
        return logName;
    }

    protected LogViewer() {
    }

    protected void init(InputOutput inputIo, FileSystem fs, String logName) {
        this.logName = logName;
        this.fs = fs;
        this.io = inputIo;
        logWriter = io.getOut();
        timer = new Timer();
        TimerTask timerTask = new DisposableTimerTask() {
            @Override
            public void run() {
                if (io.isClosed()) {
                    dispose();
                } else {
                    try {
                        updateLog();
                    } catch (Exception ex) {
                        LogHelper.logError(ex);
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        LogHelper.logInfo(this, "Log Timer scheduled");
    }

    public void dispose() {
        try {
            if (timer != null) {
                timer.cancel();
                LogHelper.logInfo(this, "Log Timer canceled");
                io.closeInputOutput();
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void updateLog() throws Exception {
        boolean res = false;
        String urlStr = logName;
        long startPos = -1;
        long lastBytes = -1;
        String action = null;
        if (lastPos > 0) {
            startPos = lastPos;
        } else {
            lastBytes = (200 * 500);
        }
        String url = "/bin/remotelog.html";
        params.clear();

        if (startPos >= 0) {
            params.put("startPos", "" + startPos);
        }

        if (lastBytes >= 0) {
            params.put("lastBytes", "" + lastBytes);
        }

        if (action != null) {
            params.put("action", action);
        }

        if (logName != null) {
            params.put("logFile", logName);
        }
        byte[] resp = fs.sendGet(url, params,false);
        InputStream is = new ByteArrayInputStream(resp);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("END_POSITION:")) {
                line = line.replaceAll("END_POSITION:", "").trim();
                long newLastPos = Long.parseLong(line);
                if (newLastPos > lastPos) {
                    lastPos = newLastPos;
                }
            } else {
                /*  if ((hideReplicationCheck.isSelected())
                 && (line.indexOf("com.day.cq.replication.Agent") >= 0)) {
                 // Skip line
                 } */
                if (line.length() > 0) {
                    res = true;
                    logWriter.append(line);
                    logWriter.append("\n");
                }
            }
        }
        br.close();
    }
}
