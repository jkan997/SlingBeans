/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.dialogs;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import java.io.File;
import java.util.Set;
import javax.swing.JFileChooser;
import org.jkan997.slingbeans.helper.ComboBoxSet;

/**
 *
 * @author jkan997
 */
public class NewNodeDialog extends javax.swing.JDialog {

    private boolean createNode = false;
    private String initialSelection = "nt:unstructured";
    private boolean lockSelection = false;
    private ComboBoxSet comboBoxSet = null;

    public NewNodeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        checkTypeIsFile();
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
        createBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        fileText = new javax.swing.JTextField();
        fileLabel = new javax.swing.JLabel();
        fileBrowseBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.title")); // NOI18N

        nodeNameText.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.nodeNameText.text")); // NOI18N

        nodeTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        nodeTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeTypeComboActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.jLabel2.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 16, Short.MAX_VALUE)
        );

        createBtn.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.createBtn.text")); // NOI18N
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.cancelBtn.text")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        fileText.setEditable(false);
        fileText.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.fileText.text")); // NOI18N

        fileLabel.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.fileLabel.text")); // NOI18N

        fileBrowseBtn.setText(org.openide.util.NbBundle.getMessage(NewNodeDialog.class, "NewNodeDialog.fileBrowseBtn.text")); // NOI18N
        fileBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fileLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, nodeTypeCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(fileText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fileBrowseBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(nodeNameText)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(134, 134, 134)
                        .add(cancelBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(createBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(fileText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(fileLabel)
                        .add(fileBrowseBtn)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(createBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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

    private void nodeTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeTypeComboActionPerformed
        checkTypeIsFile();
    }//GEN-LAST:event_nodeTypeComboActionPerformed

    
    private void checkTypeIsFile(){
        boolean typeIsFile = false;
        String nodeType = getSelectedNodeType();
        if ((nodeType!=null)&&("nt:file".equalsIgnoreCase(nodeType))){
            typeIsFile = true;
        }
        fileBrowseBtn.setEnabled(typeIsFile);
        fileText.setEnabled(typeIsFile);
        
    }
    private final JFileChooser fc = new JFileChooser();
    private  File selectedFile;

    public File getSelectedFile() {
        return selectedFile;
    }
    
    
    
    private void fileBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseBtnActionPerformed
        fc.showOpenDialog(this);
        selectedFile = fc.getSelectedFile();
        if (selectedFile!=null){
            fileText.setText(selectedFile.getAbsolutePath());
            nodeNameText.setText(selectedFile.getName());
        }
    }//GEN-LAST:event_fileBrowseBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton createBtn;
    private javax.swing.JButton fileBrowseBtn;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JTextField fileText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nodeNameText;
    private javax.swing.JComboBox nodeTypeCombo;
    // End of variables declaration//GEN-END:variables
}
