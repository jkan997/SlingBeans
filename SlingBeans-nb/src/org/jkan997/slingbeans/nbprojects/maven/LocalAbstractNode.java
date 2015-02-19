/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
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

}
