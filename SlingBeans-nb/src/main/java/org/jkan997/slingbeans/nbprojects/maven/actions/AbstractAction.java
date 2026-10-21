/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import org.jkan997.slingbeans.nbprojects.maven.LocalAbstractNode;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingRootNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public abstract class AbstractAction extends org.jkan997.slingbeans.nbactions.AbstractAction {

    protected Node node;

    public LocalSlingRootNode getRootNode() {
        if (this.node instanceof LocalSlingRootNode) {
            return (LocalSlingRootNode) this.node;
        } else {
            LocalAbstractNode node = (LocalAbstractNode) this.node;
            while ((node = (LocalAbstractNode) node.getParentNode()) != null) {
                if (node instanceof LocalSlingRootNode) {
                    return (LocalSlingRootNode) node;
                }

            }
        }
        return null;
    }

    public LocalSlingNode getLocalSlingNode() {
        if (this.node instanceof LocalSlingRootNode) {
            LocalSlingRootNode rootNode = (LocalSlingRootNode) this.node;
            Children rootNodeChildren = rootNode.getChildren();
            if (rootNodeChildren.getNodesCount() > 0) {
                return (LocalSlingNode) rootNodeChildren.getNodeAt(0);
            }
        } else {
            return (LocalSlingNode) this.node;
        }
        return null;
    }
}
