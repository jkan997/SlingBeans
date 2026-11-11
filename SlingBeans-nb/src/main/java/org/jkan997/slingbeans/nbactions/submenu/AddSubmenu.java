/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbactions.submenu;

import java.util.ArrayList;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbactions.node.AddNodeAction;
import org.jkan997.slingbeans.nbactions.property.AddPropertyAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.types.NodeTypeSet;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author jkan997
 */
public class AddSubmenu extends AbstractSubmenu {

    public AddSubmenu(AbstractNode node) {
        setActionName("Add node");
        actions = new ArrayList<AbstractAction>();
        AddNodeAction addNodeAction = new AddNodeAction(node);
        addNodeAction.setActionName("Unstructured...");
        addAction(addNodeAction);

        AddNodeAction addFileAction = new AddNodeAction(node);
        addFileAction.setActionName("File...");
        addFileAction.setInitialSelection(NodeTypeSet.NT_FILE);
        addAction(addFileAction);

        AddNodeAction addFolderAction = new AddNodeAction(node);
        addFolderAction.setActionName("Folder...");
        addFolderAction.setInitialSelection(NodeTypeSet.NT_FOLDER);
        addAction(addFolderAction);

        AddNodeAction addSlingFolderAction = new AddNodeAction(node);
        addSlingFolderAction.setActionName("Sling Folder...");
        addSlingFolderAction.setInitialSelection(NodeTypeSet.SLING_FOLDER);
        addAction(addSlingFolderAction);

    }

    public AddSubmenu() {
        this(null);
    }
}
