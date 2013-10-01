/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.dialogs;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import org.jkan997.slingbeans.configuration.Configuration;
import org.jkan997.slingbeans.configuration.ConfigurationImpl;
import org.jkan997.slingbeans.configuration.Host;
import org.jkan997.slingbeans.helper.ComboBoxSet;
import org.jkan997.slingbeans.helper.HelpHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;

public class SlingHostDialog extends javax.swing.JDialog {

    private FileSystem fileSystem = null;
    private Configuration configuration = ConfigurationImpl.getInstance();
    private Set<Host> hosts = new HashSet<Host>();
    private final ComboBoxSet hostComboBoxSet;
    
    

    public SlingHostDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //incorrectLabel.setVisible(false);

        this.loginText.setText("admin");
        this.passwdText.setText("admin");
        String defaultHost = "http://localhost:4502/";
        hosts = (Set<Host>) configuration.getObject(Configuration.HOSTS);
        hostComboBoxSet = new ComboBoxSet(hosts) {
            @Override
            protected String convertToString(Object o) {
                if ((o == null) || (!(o instanceof Host))) {
                    return "";
                }
                Host h = (Host) o;
                return h.getHostUrl();
            }
        };
        AutoCompleteSupport.install(hostText, GlazedLists.eventListOf(hostComboBoxSet.getStringArray()));
        String lastHost = (String)configuration.getObject(Configuration.LAST_HOST);
        if ((lastHost!=null)&&(lastHost.length()>0)){
            hostText.setSelectedItem(lastHost);
        } else {
            hostText.setSelectedItem(hostComboBoxSet.getFirstString());
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        loginText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        passwdText = new javax.swing.JPasswordField();
        loginBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        incorrectLabel = new javax.swing.JLabel();
        helpBtn = new javax.swing.JButton();
        hostText = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(SlingHostDialog.class, "SlingHostDialog.title")); // NOI18N
        setModal(true);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SlingHostDialog.class, "SlingHostDialog.jLabel1.text")); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel2.setText("User:"); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel3.setText("Password:"); // NOI18N
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        incorrectLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        incorrectLabel.setForeground(new java.awt.Color(255, 51, 102));
        incorrectLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        incorrectLabel.setText(org.openide.util.NbBundle.getMessage(SlingHostDialog.class, "SlingHostDialog.incorrectLabel.text")); // NOI18N

        helpBtn.setText(org.openide.util.NbBundle.getMessage(SlingHostDialog.class, "SlingHostDialog.helpBtn.text")); // NOI18N
        helpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpBtnActionPerformed(evt);
            }
        });

        hostText.setEditable(true);
        hostText.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        hostText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostTextActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(incorrectLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(helpBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loginBtn))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(17, 17, 17)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(loginText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(passwdText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(hostText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {cancelBtn, helpBtn, loginBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(hostText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(loginText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwdText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(0, 10, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(loginBtn)
                            .add(cancelBtn)
                            .add(helpBtn)))
                    .add(incorrectLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        layout.linkSize(new java.awt.Component[] {cancelBtn, helpBtn, loginBtn}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jLabel3.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(SlingHostDialog.class, "SlingHostDialog.jLabel3.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void incorrectData() {
        Runnable task = new Runnable() {
            public void run() {
                incorrectLabel.setText("Incorrect login");
            }
        };
        SwingUtilities.invokeLater(task);
    }

    private void closeDialog() {
        Runnable task = new Runnable() {
            public void run() {
                setVisible(false);
            }
        };
        SwingUtilities.invokeLater(task);
    }

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String hostUrl1 = hostText.getSelectedItem().toString();
        if (!hostUrl1.endsWith("/")) {
            hostUrl1 += "/";
        }
        final String hostUrl = hostUrl1;
        final String login = loginText.getText();
        final String password = new String(passwdText.getPassword());
        Runnable connectFsTask = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(hostUrl);
                    FileSystem fs = new FileSystem(hostUrl, login, password);
                    SlingHostDialog.this.fileSystem = fs;
                    closeDialog();
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                    incorrectData();
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(connectFsTask, "Checking Sling Repository", new AtomicBoolean(false), false);
        Host host = new Host(hostUrl, login, password);
        for (Host h : hosts) {
            if (h.getHostId().equals(host.getHostId())) {
                hosts.remove(h);
                break;
            }
        }
        hosts.add(host);
        configuration.setObject(Configuration.HOSTS, hosts);
        configuration.setObject(Configuration.LAST_HOST, host.getHostUrl());
    }//GEN-LAST:event_loginBtnActionPerformed

    private void helpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
        HelpHelper.openHelp(null);
    }//GEN-LAST:event_helpBtnActionPerformed

    private void hostTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostTextActionPerformed
        String hostStr = (String) hostText.getSelectedItem();
        if (hostStr != null) {
            Host host = (Host)hostComboBoxSet.getByLabel(hostStr);
            if (host != null) {
                loginText.setText(host.getUser());
                passwdText.setText(host.getPassword());
            }
        }

    }//GEN-LAST:event_hostTextActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SlingHostDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SlingHostDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SlingHostDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SlingHostDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SlingHostDialog dialog = new SlingHostDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton helpBtn;
    private javax.swing.JComboBox hostText;
    private javax.swing.JLabel incorrectLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginBtn;
    private javax.swing.JTextField loginText;
    private javax.swing.JPasswordField passwdText;
    // End of variables declaration//GEN-END:variables
}
