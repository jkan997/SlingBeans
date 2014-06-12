/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.property;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.dialogs.NewPropertyDialog;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;


public class AddPropertyAction extends AbstractAction {

    private SlingNode node;

    public AddPropertyAction(SlingNode node) {
        setActionName("Add property...");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewPropertyDialog npd = new NewPropertyDialog(null,true);
        SwingHelper.showDialog(npd);
        if (npd.isCreateProperty()){
            node.setProperty(npd.getPropertyName(), npd.getPropertyValue());
            node.refresh();
        }
    }
}
