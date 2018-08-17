/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.entity;

import java.util.TreeSet;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jkan997
 */
public class WorkflowSet extends TreeSet<Workflow> {

    public void init(FileSystem fs) {
        /* FOR AEM < 6.4 */
        FileObject workflowRootFo = fs.getFileObject("etc/workflow/models", 10);
        scanFolder(workflowRootFo);
        /* FOR AEM 6.4 */
        getAEM64Workflows(fs);

    }

    private void scanFolder(FileObject folder) {
        for (FileObject fo : folder.getChildren()) {
            if (fo.getPrimaryType().equals(NodeTypeSet.CQ_PAGE)) {
                addWorkflowModel(fo);
            } else {
                if (fo.isSlingFolder()) {
                    scanFolder(fo);
                }
            }
        }
    }

    private void addWorkflowModel(FileObject fo) {
        FileObject jcrContent = fo.getJcrContent();
        LogHelper.logInfo(this, "%s %s", fo.getPath(), jcrContent);
        if (jcrContent != null) {
            String title = null;
            FileObjectAttribute titleAttr = jcrContent.getAttribute("jcr:title");
            if (titleAttr != null) {
                title = titleAttr.getValue().toString();
            }
            String description = null;
            FileObjectAttribute descriptionAttr = jcrContent.getAttribute("jcr:description");
            if (descriptionAttr != null) {
                description = descriptionAttr.getValue().toString();
            }
            if (title != null) {
                Workflow w = new Workflow();
                w.setName(title);
                w.setDescription(description);
                w.setPath(fo.getPath());
                this.add(w);
            }
        }
    }

    private void getAEM64Workflows(FileSystem fs) {
        try {
            byte[] modelsBytes = fs.sendGet("/libs/cq/workflow/content/console/models.json");
            String modelsStr = new String(modelsBytes);
            JSONObject models = new JSONObject(modelsStr);
            if (models != null) {
                JSONArray modelsArr = models.getJSONArray("models");
                for (int i = 0; i < modelsArr.length(); i++) {
                    JSONObject model = modelsArr.getJSONObject(i);
                    String title = model.getString("title");
                    String description = model.getString("description");
                    String path = model.getString("item");
                    if (path.startsWith("/")){
                        path=path.substring(1);
                    }
                    Workflow w = new Workflow();
                    w.setName(title);
                    w.setDescription(description);
                    w.setPath(path);
                    this.add(w);
                }
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
