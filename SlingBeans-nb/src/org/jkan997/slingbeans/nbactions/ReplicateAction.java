/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import static javax.swing.Action.NAME;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;

@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.ReplicateAction")
@ActionRegistration(asynchronous = true, displayName = "Replicate")
public class ReplicateAction extends AbstractAction {

    private SlingNode node;

    public ReplicateAction(SlingNode node) {
        setActionName("Replicate");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Runnable loadWorkflowsTask = new Runnable() {
            @Override
            public void run() {
                try {
                    replicateNode();
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(loadWorkflowsTask, "Replicating node", new AtomicBoolean(false), false);
    }

    private void replicateNode() {
        try {
            FileObject fo = node.getFileObject();
            FileSystem fs = fo.getFileSystem();
            logInfo("Node path: %s", fo.getPath());
            Map<String, String> params = new TreeMap<String, String>();
            params.put("action", "replicate");
            params.put("path", "/" + fo.getPath());
            fs.sendSimplePost("/crx/de/replication.jsp", params);
            Thread.sleep(1000);
            params.remove("action");
            params.put("path", "/" + fo.getPath());
            byte[] res = null;
            res = fs.sendGet("/crx/de/replication.jsp", params);
            String buildLog = new String(res);
            buildLog = buildLog.replace("{", "{\n");
            buildLog = buildLog.replace(",\"", ",\n\"");
            logHeader("REPLICATING NODE");
            logInfo(buildLog);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
