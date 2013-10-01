/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.dialogs;


import java.awt.Color;
import org.jkan997.slingbeans.sync.SyncMode;
import org.jkan997.slingbeans.sync.Synchronizer;
import org.jkan997.slingbeans.test.SyncEntryTableModel;

/**
 *
 * @author jakaniew
 */
public class SlingSyncConflictDialog extends javax.swing.JDialog {

    private Synchronizer sync;

    public SlingSyncConflictDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Synchronization confirmation");
    }

    public Synchronizer getSync() {
        return sync;
    }

    public void setSync(Synchronizer sync) {
        this.sync = sync;
        SyncEntryTableModel setm = new SyncEntryTableModel();
        setm.setSyncEntries(sync.getDescriptor().entries());
        syncTable.setModel(setm);
        boolean hasConflicts = sync.hasConflict();
        boolean hasRemovals = sync.hasRemovals();
        if ((!hasConflicts) && (!hasRemovals)) {
            msgText.setText("Your changes has no conflicts and removals");
            msgText.setForeground(Color.BLACK);
        } else {
            StringBuilder sb = new StringBuilder();
            msgText.setText("Your changes has conflicts and/or removals, please check before sync");
            msgText.setForeground(Color.RED);
        }
        this.synchronizeBtn.setEnabled(!hasConflicts);
        this.synchronizeLocalBtn.setEnabled(hasConflicts);
        this.synchronizeRemoteBtn.setEnabled(hasConflicts);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        msgText = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        synchronizeBtn = new javax.swing.JButton();
        synchronizeLocalBtn = new javax.swing.JButton();
        synchronizeRemoteBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        syncTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        msgText.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        msgText.setText(org.openide.util.NbBundle.getMessage(SlingSyncConflictDialog.class, "SlingSyncConflictDialog.msgText.text")); // NOI18N

        synchronizeBtn.setText(org.openide.util.NbBundle.getMessage(SlingSyncConflictDialog.class, "SlingSyncConflictDialog.synchronizeBtn.text")); // NOI18N
        synchronizeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synchronizeBtnActionPerformed(evt);
            }
        });

        synchronizeLocalBtn.setText(org.openide.util.NbBundle.getMessage(SlingSyncConflictDialog.class, "SlingSyncConflictDialog.synchronizeLocalBtn.text")); // NOI18N
        synchronizeLocalBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synchronizeLocalBtnActionPerformed(evt);
            }
        });

        synchronizeRemoteBtn.setText(org.openide.util.NbBundle.getMessage(SlingSyncConflictDialog.class, "SlingSyncConflictDialog.synchronizeRemoteBtn.text")); // NOI18N
        synchronizeRemoteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synchronizeRemoteBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(synchronizeBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(synchronizeLocalBtn)
                .add(0, 0, 0)
                .add(synchronizeRemoteBtn)
                .add(0, 0, 0)
                .add(cancelBtn)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanelLayout.linkSize(new java.awt.Component[] {cancelBtn, synchronizeBtn, synchronizeLocalBtn, synchronizeRemoteBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(synchronizeBtn)
            .add(synchronizeLocalBtn)
            .add(synchronizeRemoteBtn)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, cancelBtn)
        );

        mainPanelLayout.linkSize(new java.awt.Component[] {cancelBtn, synchronizeBtn, synchronizeLocalBtn, synchronizeRemoteBtn}, org.jdesktop.layout.GroupLayout.VERTICAL);

        syncTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(syncTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                    .add(msgText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(msgText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void synchronizeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synchronizeBtnActionPerformed
        sync.synchronize(null);
        setVisible(false);
    }//GEN-LAST:event_synchronizeBtnActionPerformed

    private void synchronizeLocalBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synchronizeLocalBtnActionPerformed
        sync.synchronize(SyncMode.USE_LOCAL_CHANGES);
        setVisible(false);
    }//GEN-LAST:event_synchronizeLocalBtnActionPerformed

    private void synchronizeRemoteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synchronizeRemoteBtnActionPerformed
        sync.synchronize(SyncMode.USE_REMOTE_CHANGES);
        setVisible(false);
    }//GEN-LAST:event_synchronizeRemoteBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

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
            java.util.logging.Logger.getLogger(SlingSyncConflictDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SlingSyncConflictDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SlingSyncConflictDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SlingSyncConflictDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SlingSyncConflictDialog dialog = new SlingSyncConflictDialog(new javax.swing.JFrame(), true);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel msgText;
    private javax.swing.JTable syncTable;
    private javax.swing.JButton synchronizeBtn;
    private javax.swing.JButton synchronizeLocalBtn;
    private javax.swing.JButton synchronizeRemoteBtn;
    // End of variables declaration//GEN-END:variables
}
