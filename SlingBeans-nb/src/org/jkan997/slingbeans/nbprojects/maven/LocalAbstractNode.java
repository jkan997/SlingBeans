/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author jakaniew
 */
public abstract class LocalAbstractNode extends AbstractNode {

    public LocalAbstractNode(Children children) {
        super(children);
    }

    public LocalAbstractNode(Children children, Lookup lookup) {
        super(children, lookup);
    }

    public LocalAbstractNode getChildByName(String name) {
        Node[] nodes = this.getChildren().getNodes();
        Node res = null;
        for (Node n : nodes) {
            if (n.getDisplayName().equals(name)) {
                res = n;
                break;
            }
        }
        return (LocalAbstractNode) res;
    }

}
