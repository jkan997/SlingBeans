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
public class FileObjectNameComparator implements Comparator<FileObject> {
    public final static FileObjectNameComparator instance = new FileObjectNameComparator();

    @Override
    public int compare(FileObject fo1, FileObject fo2) {
       return (fo1.getNameExt().compareTo(fo2.getNameExt()));
    }
}
