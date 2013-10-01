/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.dialogs;

import org.jkan997.slingbeans.slingfs.FileObject;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author jakaniew
 */
public class TreeModel implements javax.swing.tree.TreeModel{

    private FileObject root;

    
    public void setRoot(FileObject root) {
        this.root = root;
    }
    
    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        FileObject fo = (FileObject)parent;
        return fo.getChildren()[index];
    }

    @Override
    public int getChildCount(Object parent) {
        FileObject fo = (FileObject)parent;
        return fo.getChildren().length;
    }

    @Override
    public boolean isLeaf(Object node) {
        FileObject fo = (FileObject)node;
        return (fo.getChildren().length==0);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        FileObject[] children = ((FileObject)parent).getChildren();
        for (int i = 0;i<children.length; i++){
            FileObject cFo = children[i];
            if (cFo.equals(child)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
    
}
