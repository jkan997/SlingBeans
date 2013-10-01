/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import org.jkan997.slingbeans.nbtree.SlingNode;


public class OpenCrxDeAction extends OpenBrowserAction {

    public OpenCrxDeAction(SlingNode node) {
        super(node);
        this.openBrowserMode = OpenBrowserAction.OPEN_BROWSER_MODE_CRXDE;
        this.setActionName("Open in CRXDE Lite");
    }
}
