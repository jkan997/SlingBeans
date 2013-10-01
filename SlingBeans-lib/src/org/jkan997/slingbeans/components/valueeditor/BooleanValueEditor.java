/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components.valueeditor;

import javax.swing.JCheckBox;

/**
 *
 * @author jkan997
 */
public class BooleanValueEditor extends JCheckBox implements ValueEditor {

    public BooleanValueEditor(Object value) {
        this.setValue(value);
    }

    @Override
    public void init() {
    }

    @Override
    public boolean isValidValue() {
        return true;
    }

    @Override
    public Object getValue() {
        return this.isSelected();
    }

    @Override
    public void setValue(Object val) {
        this.setSelected((Boolean)val);
    }
}
