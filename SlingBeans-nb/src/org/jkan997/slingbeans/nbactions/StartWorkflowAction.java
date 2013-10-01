package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jkan997.slingbeans.configuration.Configuration;
import org.jkan997.slingbeans.configuration.ConfigurationImpl;
import org.jkan997.slingbeans.dialogs.StartWorkflowDialog;
import org.jkan997.slingbeans.entity.WorkflowConfiguration;
import org.jkan997.slingbeans.entity.WorkflowSet;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.loaders.DataObject;

public class StartWorkflowAction extends AbstractAction {

    private FileObject fileObject;
    private WorkflowSet workflows;
    protected boolean alwaysShowDialog = false;

    public StartWorkflowAction(DataObject context) {
        setActionName("Start workflow");
        /*  if (context!=null){
         org.openide.filesystems.FileObject fo = context.getPrimaryFile();
         File f = Utilities.toFile(fo.toURI());
         File syncedFolder = SyncDescriptor.findSyncDescriptor(f);
         this.setFileObject(syncedFolder);
         }*/
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    private void loadWorkflows() {
        final Runnable loadWorkflowsTask = new Runnable() {
            @Override
            public void run() {
                try {
                    FileSystem fs = fileObject.getFileSystem();
                    workflows = fs.readWorkflows();
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(loadWorkflowsTask, "Loading workflows", new AtomicBoolean(false), false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            StartWorkflowDialog swd = new StartWorkflowDialog(null, true);
            String payload = null;
            if (fileObject.getParent() != null) {
                payload = "/" + fileObject.getPath();
            }
            Configuration configuration = ConfigurationImpl.getInstance();
            WorkflowConfiguration workflowConfiguration = (WorkflowConfiguration) configuration.getObject(WorkflowConfiguration.WORKFLOW_CONFIGURATION);
            if ((this.alwaysShowDialog) || (workflowConfiguration == null)) {
                loadWorkflows();
                swd.init(workflows, payload);
                SwingHelper.showDialog(swd);
                if (swd.isStartWorkflow()) {
                    startWorkflow(swd.getWorkflowConfiguration());
                }
            } else {
                startWorkflow(workflowConfiguration);
            }

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }

    }

    private void startWorkflow(final WorkflowConfiguration wc) {
        logHeader("STARTING WORKFLOW %s", wc.getWorkfloadName());
        final Runnable startWorkflowTask = new Runnable() {
            @Override
            public void run() {
                try {
                    FileSystem fs = fileObject.getFileSystem();
                    Map<String, String> params = new LinkedHashMap<String, String>();
                    params.put("model", "/" + wc.getWorkfloadPath() + "/jcr:content/model");
                    params.put("payload", wc.getWorkflowPayload());
                    params.put("payloadType", "JCR_PATH");
                    params.put("workflowTitle", wc.getWorkflowTitle());
                    params.put("startComment", wc.getWorkfloadComment());
                    logInfo("Workflow params:");
                    for (Map.Entry<String, String> me : params.entrySet()) {
                        logInfo("%s = %s", me.getKey(), me.getValue());
                    }
                    String url = "/etc/workflow/instances";
                    byte[] getResp = fs.sendSimplePost(url, params);
                    String getRespStr = new String(getResp);
                    logInfo(getRespStr);
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(startWorkflowTask, "Starting workflow", new AtomicBoolean(false), false);
    }
}
