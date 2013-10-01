/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbtree;


import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author jakaniew
 */
public class SlingNodeChildren extends Children.Keys {

    FileObject parentObject = null;

    public SlingNodeChildren(FileObject parentObject) {
        this.parentObject = parentObject;
    }

    public SlingNodeChildren() {
    }

    @Override
    protected void addNotify() {
        FileObject[] children = parentObject.getChildren();
        setKeys(children);
    }
    
    
    public void reload(){
        parentObject.setChildrenLoaded(false);
        setKeys(new Object[]{});
        FileObject[] children = parentObject.getChildren();
        setKeys(children);
    }
    
    @Override
    protected Node[] createNodes(Object o) {
        return loadNodes(o,false);
    }

    protected Node[] loadNodes(Object o,boolean refresh) {
        FileObject fo = (FileObject)o;
        if (refresh){
            fo.refresh();
        }
        LogHelper.logInfo(this,fo.toString());
        Children children = null;
        if (fo.isLeafNode()){
            children = Children.LEAF;
        } else {
            children = new SlingNodeChildren(fo);
        }
        SlingNode crxNode = new SlingNode(children, Lookups.singleton(fo));
        crxNode.setFileObject(fo);
       // result.set
        String displayName = fo.getNameExt();
        if (displayName.length()==0) displayName="/";
        crxNode.setDisplayName(displayName);
        return new Node[]{crxNode};
    }
}
