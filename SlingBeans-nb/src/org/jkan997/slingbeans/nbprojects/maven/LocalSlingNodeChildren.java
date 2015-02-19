/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbprojects.maven;

import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbtree.*;
import org.jkan997.slingbeans.slingfs.local.LocalFileObject;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author jkan997
 */
public class LocalSlingNodeChildren extends Children.Keys {

    LocalFileObject parentObject = null;

    public LocalSlingNodeChildren(LocalFileObject parentObject) {
        this.parentObject = parentObject;
    }

    public LocalSlingNodeChildren() {
    }

    @Override
    protected void addNotify() {
        LocalFileObject[] children = new LocalFileObject[]{};
        if (parentObject != null) {
            children = parentObject.getChildren();
        }
        setKeys(children);
    }

    public void reload() {
        LocalFileObject[] children = new LocalFileObject[]{};
        if (parentObject != null) {
            setKeys(new Object[]{});
            children = parentObject.getChildren();
        }
        setKeys(children);
    }

    @Override
    protected Node[] createNodes(Object o) {

        return loadNodes(o, false);
    }

    protected Node[] loadNodes(Object o, boolean refresh) {
        LocalFileObject fo = (LocalFileObject) o;
        if (refresh) {
            //fo.refresh();
        }
        LogHelper.logInfo(this, fo.toString());
        Children children = null;
        if (fo.isLeafNode()) {
            children = Children.LEAF;
        } else {
            children = new LocalSlingNodeChildren(fo);
        }
        LocalSlingNode localSlingNode = new LocalSlingNode(fo,children);
        // result.set
        String displayName = fo.getFullName();
        if (displayName.length() == 0) {
            displayName = "/";
        }
        localSlingNode.setDisplayName(displayName);
        return new Node[]{localSlingNode};
    }
}
