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
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;

/**
 *
 * @author jkan997
 */
public class WorkflowSet extends TreeSet<Workflow> {

    public void init(FileObject workflowRootFo) {
        scanFolder(workflowRootFo);
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
}
