/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.dialogs;

import javax.swing.DefaultComboBoxModel;

public class RemovePropertyDialog extends javax.swing.JDialog {

    private boolean removeProperty;

    public RemovePropertyDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        propertyTypeCombo.setSelectedIndex(0);
    }

    public void init() {
    }

    public void setProperties(String[] props) {
        propertyTypeCombo.setModel(new DefaultComboBoxModel(props));
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
        cancelBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        propertyTypeCombo = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.title")); // NOI18N
        setResizable(false);

        cancelBtn.setText(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.cancelBtn.text")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        removeBtn.setText(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.removeBtn.text")); // NOI18N
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorLabel.setText(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.errorLabel.text")); // NOI18N
        errorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.errorLabel.toolTipText")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(removeBtn)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {cancelBtn, removeBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(removeBtn)
                            .add(cancelBtn))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(org.openide.util.NbBundle.getMessage(RemovePropertyDialog.class, "RemovePropertyDialog.jLabel5.text")); // NOI18N

        propertyTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        propertyTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propertyTypeComboActionPerformed(evt);
            }
        });
        propertyTypeCombo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                propertyTypeComboPropertyChange(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(propertyTypeCombo, 0, 213, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(propertyTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void propertyTypeComboPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_propertyTypeComboPropertyChange
    }//GEN-LAST:event_propertyTypeComboPropertyChange

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        this.removeProperty = true;
        this.setVisible(false);
    }//GEN-LAST:event_removeBtnActionPerformed

    private void propertyTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertyTypeComboActionPerformed
    }//GEN-LAST:event_propertyTypeComboActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    public String getProperty() {
        return propertyTypeCombo.getSelectedItem().toString();
    }

    public boolean isRemoveProperty() {
        return removeProperty;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox propertyTypeCombo;
    private javax.swing.JButton removeBtn;
    // End of variables declaration//GEN-END:variables
}
