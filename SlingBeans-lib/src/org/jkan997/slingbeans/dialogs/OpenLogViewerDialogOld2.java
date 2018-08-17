/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.dialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import org.jkan997.slingbeans.configuration.Configuration;
import org.jkan997.slingbeans.configuration.ConfigurationImpl;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.RemoteLogInstaller;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;

public class OpenLogViewerDialogOld2 extends javax.swing.JDialog {

    private boolean logFilesSelected = false;
    private Set<String> selectedLogFiles = null;
    private List<String> logFiles = null;
    private final static String LOG_FILES = "log-files";
    private List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
    private FileSystem fs;

    public OpenLogViewerDialogOld2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void createCheckBox(String logFile) {
        JCheckBox cb = new JCheckBox();
        cb.setText(logFile);
        cb.setName(logFile);
        if (selectedLogFiles.contains(logFile)) {
            cb.setSelected(true);
        }
        checkboxes.add(cb);
        logPanel.add(cb);
    }

    protected void installRemoteLog() {

        try {
            RemoteLogInstaller rli = new RemoteLogInstaller(fs);
            rli.installRemoteLog();
        } catch (IOException ex) {
            LogHelper.logError(ex);
        }
    }

    public void loadLogFilesInternal() {
        try {
            String url = "/bin/remotelog.html";
            Map<String, String> params = new HashMap<String, String>();
            params.put("action", "list");
            byte[] resp = fs.sendGet(url, params);
            String str = new String(resp);
            if (!str.contains("<")) {
                ByteArrayInputStream is = new ByteArrayInputStream(resp);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.contains("No resource found")) {
                        logFiles.clear();
                        return;
                    }
                    if (!firstLine) {
                        logFiles.add(line);
                    } else {
                        firstLine = false;
                    }
                }
                br.close();
            }

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    public void loadLogFiles(final boolean installRemoteLog) {
        
        logFiles = new ArrayList<String>();

        final Runnable logFileLoadedRunnable = new Runnable() {
            public void run() {
                logFilesLoaded();
            }
        };

        final Runnable loadLogFilesTask = new Runnable() {
            @Override
            public void run() {
                if (installRemoteLog) {
                    installRemoteLog();
                }
                loadLogFilesInternal();
                java.awt.EventQueue.invokeLater(logFileLoadedRunnable);
            }

        };
        ProgressUtils.runOffEventDispatchThread(loadLogFilesTask, "Loading log file list", new AtomicBoolean(false), false);
    }

    public void init(FileSystem fs) {
        this.fs = fs;
        loadLogFiles(false);
    }

    protected void logFilesLoaded() {
        try {
            Configuration configuration = ConfigurationImpl.getInstance();
            selectedLogFiles = (Set<String>) configuration.getObject(LOG_FILES);
            if (selectedLogFiles == null) {
                selectedLogFiles = new TreeSet<String>();
            }
            logPanel.removeAll();
            if ((logFiles == null) || (logFiles.size() == 0)) {
                GridLayout gl = (GridLayout) logPanel.getLayout();
                gl.setRows(1);
                JButton remoteLogBtn = new JButton();
                remoteLogBtn.setMaximumSize(new Dimension(200, 50));
                remoteLogBtn.setText("Install remote log on AEM");
                logPanel.add(remoteLogBtn);
                remoteLogBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        loadLogFiles(true);
                    }

                });
            } else {

                for (String logFile : logFiles) {
                    GridLayout gl = (GridLayout) logPanel.getLayout();
                    gl.setRows(logFiles.size());
                    createCheckBox(logFile);
                }

            }

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        cancelBtn1 = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        selectBtn = new javax.swing.JButton();
        listScrollPane = new javax.swing.JScrollPane();
        logPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.title")); // NOI18N
        setResizable(false);

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorLabel.setText(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.errorLabel.text")); // NOI18N
        errorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.errorLabel.toolTipText")); // NOI18N

        cancelBtn1.setText(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.cancelBtn1.text")); // NOI18N
        cancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtn1ActionPerformed(evt);
            }
        });

        cancelBtn.setText(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.cancelBtn.text")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        selectBtn.setText(org.openide.util.NbBundle.getMessage(OpenLogViewerDialogOld2.class, "OpenLogViewerDialogOld2.selectBtn.text")); // NOI18N
        selectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(cancelBtn1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(0, 0, 0))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {cancelBtn, cancelBtn1, selectBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cancelBtn1)
                            .add(cancelBtn)
                            .add(selectBtn))
                        .add(0, 0, Short.MAX_VALUE))))
        );

        logPanel.setLayout(new java.awt.GridLayout(100, 0));
        listScrollPane.setViewportView(logPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(listScrollPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(listScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBtnActionPerformed
        selectedLogFiles.clear();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selectedLogFiles.add(cb.getName());
            }
        }
        Configuration configuration = ConfigurationImpl.getInstance();
        configuration.setObject(LOG_FILES, selectedLogFiles);
        logFilesSelected = true;
        this.setVisible(false);

    }//GEN-LAST:event_selectBtnActionPerformed

    public boolean isLogFilesSelected() {
        return logFilesSelected;
    }

    public Set<String> getSelectedLogFiles() {
        return selectedLogFiles;
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton cancelBtn1;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JPanel logPanel;
    private javax.swing.JButton selectBtn;
    // End of variables declaration//GEN-END:variables

    private void showInstallRemoteLog() {
    }
}
