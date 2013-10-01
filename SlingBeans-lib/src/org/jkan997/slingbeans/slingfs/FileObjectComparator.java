/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs;

import java.util.Comparator;

/**
 *
 * @author jakaniew
 */
public class FileObjectComparator implements Comparator {

    public final static FileObjectComparator instance = new FileObjectComparator();
    
    @Override
    public int compare(Object o1, Object o2) {
        FileObject fo1 = (FileObject)o1;
        FileObject fo2 = (FileObject)o2;
        return fo1.getName().compareTo(fo2.getName());
    }
    
}
