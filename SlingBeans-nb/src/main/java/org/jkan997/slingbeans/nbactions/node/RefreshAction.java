/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.node;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;


public class RefreshAction extends AbstractAction {

    private SlingNode node;

    public RefreshAction(SlingNode node) {
        setActionName("Refresh");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        node.refresh();
    }
}
