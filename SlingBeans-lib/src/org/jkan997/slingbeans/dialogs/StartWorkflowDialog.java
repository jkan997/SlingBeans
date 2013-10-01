/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.dialogs;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import java.util.Set;
import org.jkan997.slingbeans.configuration.Configuration;
import org.jkan997.slingbeans.configuration.ConfigurationImpl;
import org.jkan997.slingbeans.entity.WorkflowConfiguration;
import org.jkan997.slingbeans.entity.Workflow;
import org.jkan997.slingbeans.helper.ComboBoxSet;

/**
 *
 * @author jakaniew
 */
public class StartWorkflowDialog extends javax.swing.JDialog {

    private boolean startWorkflow = false;
    private ComboBoxSet comboBoxSet = null;
    private WorkflowConfiguration workflowConfiguration;

    public StartWorkflowDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public void init(Set workflows,String payload) {
        comboBoxSet = new ComboBoxSet(workflows, true) {
            @Override
            protected String convertToString(Object o) {
                Workflow w = (Workflow) o;
                if (w == null) {
                    return "";
                }
                return w.getName();
            }
        };
        AutoCompleteSupport.install(workflowCombo, GlazedLists.eventListOf(comboBoxSet.getStringArray()));

        Configuration configuration = ConfigurationImpl.getInstance();
        workflowConfiguration = (WorkflowConfiguration) configuration.getObject(WorkflowConfiguration.WORKFLOW_CONFIGURATION);
        if (workflowConfiguration == null) {
            workflowConfiguration = new WorkflowConfiguration();
            workflowConfiguration.setWorkfloadName(comboBoxSet.getFirstString());
            workflowConfiguration.setWorkflowPayload("/some/payload");
            workflowConfiguration.setWorkflowTitle("Workflow title");
            workflowConfiguration.setWorkfloadComment("Workflow comment");
        }
        if (payload!=null){
            workflowConfiguration.setWorkflowPayload(payload);
        }
        workflowCombo.setSelectedItem(workflowConfiguration.getWorkfloadName());
        workflowPayloadText.setText(workflowConfiguration.getWorkflowPayload());
        workflowTitleText.setText(workflowConfiguration.getWorkflowTitle());
        workflowDescriptionText.setText(workflowConfiguration.getWorkfloadComment());
    }

    public boolean isStartWorkflow() {
        return startWorkflow;
    }

    public WorkflowConfiguration getWorkflowConfiguration() {
        return workflowConfiguration;
    }

    

    public String getSelectedNodeType() {
        Object selected = workflowCombo.getSelectedItem();
        return (selected != null ? selected.toString() : null);
    }

    public String getSelectedNodeName() {
        return workflowPayloadText.getText();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        workflowCombo = new javax.swing.JComboBox();
        workflowPayloadText = new javax.swing.JTextField();
        workflowTitleText = new javax.swing.JTextField();
        workflowDescriptionPane = new javax.swing.JScrollPane();
        workflowDescriptionText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        startBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.title_1")); // NOI18N

        workflowCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        workflowPayloadText.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.workflowPayloadText.text_1")); // NOI18N

        workflowTitleText.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.workflowTitleText.text")); // NOI18N

        workflowDescriptionText.setColumns(20);
        workflowDescriptionText.setRows(5);
        workflowDescriptionPane.setViewportView(workflowDescriptionText);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.jLabel1.text_1")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.jLabel2.text_1")); // NOI18N

        cancelBtn.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.cancelBtn.text_1")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        startBtn.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.startBtn.text_1")); // NOI18N
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
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
                .add(startBtn)
                .add(0, 0, 0))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {cancelBtn, startBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn)
                    .add(startBtn))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(StartWorkflowDialog.class, "StartWorkflowDialog.jLabel4.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(workflowPayloadText)
                            .add(workflowCombo, 0, 307, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(workflowDescriptionPane)
                            .add(workflowTitleText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))))
                .addContainerGap())
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(workflowCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(workflowPayloadText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(workflowTitleText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(workflowDescriptionPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        
        Configuration configuration = ConfigurationImpl.getInstance();
        Workflow workflow = (Workflow) comboBoxSet.getByLabel(workflowCombo.getSelectedItem().toString());
        if (workflow!=null){
        workflowConfiguration.setWorkfloadName(workflow.getName());
        workflowConfiguration.setWorkfloadPath(workflow.getPath());
        workflowConfiguration.setWorkflowPayload(workflowPayloadText.getText());
        workflowConfiguration.setWorkflowTitle(workflowTitleText.getText());
        workflowConfiguration.setWorkfloadComment(workflowDescriptionText.getText());
        startWorkflow = true;
        configuration.setObject(WorkflowConfiguration.WORKFLOW_CONFIGURATION, workflowConfiguration);
        }
        this.setVisible(false);

    }//GEN-LAST:event_startBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton startBtn;
    private javax.swing.JComboBox workflowCombo;
    private javax.swing.JScrollPane workflowDescriptionPane;
    private javax.swing.JTextArea workflowDescriptionText;
    private javax.swing.JTextField workflowPayloadText;
    private javax.swing.JTextField workflowTitleText;
    // End of variables declaration//GEN-END:variables
}
