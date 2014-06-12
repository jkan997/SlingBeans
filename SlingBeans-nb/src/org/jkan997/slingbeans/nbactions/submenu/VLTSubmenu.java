/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbactions.OpenBrowserAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author jkan997
 */
public class VLTSubmenu extends AbstractSubmenu {
    
    public VLTSubmenu(AbstractNode node) {
        this(node, null);
    }
    
    public VLTSubmenu(AbstractNode node, FileObject fileObject) {
        setActionName("VLT");
        actions = new ArrayList<AbstractAction>();
        if (node instanceof SlingNode) {
        /*    openWfConsoleAction.setActionName("Open Workflow console");
            openWfConsoleAction.openBrowserMode = OpenBrowserAction.OPEN_BROWSER_MODE_WF_CONSOLE;
            addAction(openWfConsoleAction);*/
        }    
            
    }
    
    public VLTSubmenu() {
        this(null);
    }
}
