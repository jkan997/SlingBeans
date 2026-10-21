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
public class FileObjectNameComparator implements Comparator<FileObject> {
    public final static FileObjectNameComparator instance = new FileObjectNameComparator();

    @Override
    public int compare(FileObject fo1, FileObject fo2) {
       return (fo1.getNameExt().compareTo(fo2.getNameExt()));
    }
}
