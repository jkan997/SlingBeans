/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.entity;

import java.io.Serializable;

public class WorkflowConfiguration implements Serializable {

    public final static String WORKFLOW_CONFIGURATION = "workflow-configuration";

    private String workfloadName;
    private String workfloadPath;
    private String workflowPayload;
    private String workflowTitle;
    private String workfloadComment;

    public String getWorkfloadName() {
        return workfloadName;
    }

    public void setWorkfloadName(String workfloadName) {
        this.workfloadName = workfloadName;
    }

    public String getWorkflowPayload() {
        return workflowPayload;
    }

    public void setWorkflowPayload(String workflowPayload) {
        this.workflowPayload = workflowPayload;
    }

    public String getWorkflowTitle() {
        return workflowTitle;
    }

    public void setWorkflowTitle(String workflowTitle) {
        this.workflowTitle = workflowTitle;
    }

    public String getWorkfloadComment() {
        return workfloadComment;
    }

    public void setWorkfloadComment(String workfloadComment) {
        this.workfloadComment = workfloadComment;
    }

    public String getWorkfloadPath() {
        return workfloadPath;
    }

    public void setWorkfloadPath(String workfloadPath) {
        this.workfloadPath = workfloadPath;
    }
}
