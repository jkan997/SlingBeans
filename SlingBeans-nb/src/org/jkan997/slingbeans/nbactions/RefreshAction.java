package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
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
