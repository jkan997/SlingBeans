/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.dialogs;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import java.util.Set;
import org.jkan997.slingbeans.helper.ComboBoxSet;
import org.jkan997.slingbeans.slingfs.types.NodeType;

/**
 *
 * @author jakaniew
 */
public class NewNodeDialog extends javax.swing.JDialog {

    private boolean createNode = false;
    private String initialSelection = "nt:unstructured";
    private boolean lockSelection = false;
    private ComboBoxSet comboBoxSet = null;

    public NewNodeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public void setNodeTypes(Set nodeTypes) {
        comboBoxSet = new ComboBoxSet(nodeTypes, true);
        AutoCompleteSupport.install(nodeTypeCombo, GlazedLists.eventListOf(comboBoxSet.getStringArray()));
        nodeTypeCombo.setSelectedItem(initialSelection);
        if (lockSelection){
            nodeTypeCombo.setEnabled(false);
        }
    }

    public boolean isCreateNode() {
        return createNode;
    }

    public String getSelectedNodeType() {
        Object selected = nodeTypeCombo.getSelectedItem();
        return (selected != null ? selected.toString() : null);
    }

    public String getSelectedNodeName() {
        return nodeNameText.getText();
    }

    public String getInitialSelection() {
        return initialSelection;
    }

    public void setInitialSelection(String initialSelection) {
        this.initialSelection = initialSelection;
    }

    public boolean isLockSelection() {
        return lockSelection;
    }

    public void setLockSelection(boolean lockSelection) {
        this.lockSelection = lockSelection;
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nodeNameText = new javax.swing.JTextField();
        nodeTypeCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        createBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.title")); // NOI18N

        nodeNameText.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.nodeNameText.text")); // NOI18N

        nodeTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.jLabel2.text")); // NOI18N

        cancelBtn.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.cancelBtn.text")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        createBtn.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.createBtn.text")); // NOI18N
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(cancelBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(createBtn)
                .add(0, 0, 0))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {cancelBtn, createBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn)
                    .add(createBtn)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(nodeNameText)
                            .add(nodeTypeCombo, 0, 253, Short.MAX_VALUE)))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nodeNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nodeTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        createNode = true;
        this.setVisible(false);

    }//GEN-LAST:event_createBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton createBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nodeNameText;
    private javax.swing.JComboBox nodeTypeCombo;
    // End of variables declaration//GEN-END:variables
}
