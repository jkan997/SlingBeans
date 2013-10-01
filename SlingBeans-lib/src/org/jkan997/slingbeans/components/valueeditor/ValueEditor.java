/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components.valueeditor;

/**
 *
 * @author jkan997
 */
public interface ValueEditor {

    public void init();
    
    public boolean isValidValue();

    public Object getValue();

    public void setValue(Object val);
}
