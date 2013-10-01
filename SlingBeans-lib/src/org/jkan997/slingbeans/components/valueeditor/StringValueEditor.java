/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components.valueeditor;

import javax.swing.JTextField;

/**
 *
 * @author jkan997
 */
public class StringValueEditor extends JTextField implements ValueEditor {

    public StringValueEditor(String text) {
        setValue(text);
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
        return this.getText();
    }

    @Override
    public void setValue(Object val) {
        this.setText(val.toString());
    }
}
