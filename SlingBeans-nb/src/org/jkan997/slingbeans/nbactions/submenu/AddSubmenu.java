/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbactions.AddNodeAction;
import org.jkan997.slingbeans.nbactions.AddPropertyAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;

/**
 *
 * @author jkan997
 */
public class AddSubmenu extends AbstractSubmenu {

    public AddSubmenu(SlingNode node) {
        setActionName("Add");
        actions = new ArrayList<AbstractAction>();
        AddNodeAction addNodeAction = new AddNodeAction(node);
        addNodeAction.setActionName("Add node...");
        addAction(addNodeAction);

        AddNodeAction addFileAction = new AddNodeAction(node);
        addFileAction.setActionName("Add file...");
        addFileAction.setInitialSelection(NodeTypeSet.NT_FILE);
        addAction(addFileAction);

        AddNodeAction addFolderAction = new AddNodeAction(node);
        addFolderAction.setActionName("Add folder...");
        addFolderAction.setInitialSelection(NodeTypeSet.NT_FOLDER);
        addAction(addFolderAction);

        AddNodeAction addSlingFolderAction = new AddNodeAction(node);
        addSlingFolderAction.setActionName("Add Sling folder...");
        addSlingFolderAction.setInitialSelection(NodeTypeSet.SLING_FOLDER);
        addAction(addSlingFolderAction);


        AddPropertyAction addPropertyAction = new AddPropertyAction(node);
        addAction(addPropertyAction);

    }

    public AddSubmenu() {
        this(null);
    }
}
