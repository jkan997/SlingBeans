/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;

/**
 *
 * @author jkan997
 */
public class RemoveSubmenu extends AbstractSubmenu {

    public RemoveSubmenu(SlingNode node) {
        setActionName("Remove");
        actions = new ArrayList<AbstractAction>();
        
        if (node instanceof SlingNode) {
        //    addAction(replicateAction);
        }

    }

    public RemoveSubmenu() {
        this(null);
    }
}
