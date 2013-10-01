/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import org.jkan997.slingbeans.nbactions.submenu.AbstractSubmenu;
import java.util.ArrayList;
import org.jkan997.slingbeans.nbtree.SlingNode;

/**
 *
 * @author jkan997
 */
public class AddNodeSubmenu extends AbstractSubmenu{
    
    public AddNodeSubmenu(SlingNode node) {
        setActionName("Workflows");
        actions = new ArrayList<AbstractAction>();
        StartWorkflowAction startWorkflowAction = new StartWorkflowAction(null);
        startWorkflowAction.setFileObject(node.getFileObject());
        StartWorkflowWithDialogAction startWorkflowWithDialogAction = new StartWorkflowWithDialogAction(null);
        startWorkflowWithDialogAction.setFileObject(node.getFileObject());
        addAction(startWorkflowAction);
        addAction(startWorkflowWithDialogAction);
    }

    public AddNodeSubmenu() {
        this(null);
    }
}
