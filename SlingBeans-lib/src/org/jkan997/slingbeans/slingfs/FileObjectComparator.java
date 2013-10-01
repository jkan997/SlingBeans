/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.slingfs;

import java.util.Comparator;

/**
 *
 * @author jkan997
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
