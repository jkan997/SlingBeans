package org.jkan997.slingbeans.nbactions;

import org.jkan997.slingbeans.nbtree.SlingNode;


public class OpenCrxDeAction extends OpenBrowserAction {

    public OpenCrxDeAction(SlingNode node) {
        super(node);
        this.openBrowserMode = OpenBrowserAction.OPEN_BROWSER_MODE_CRXDE;
        this.setActionName("Open in CRXDE Lite");
    }
}
