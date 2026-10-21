/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jkan997
 */
public class BooleanValueEditor extends JComboBox implements ValueEditor {

    private final static String TRUE = "true";
    private final static String FALSE = "false";
    private final static String[] options = {FALSE, TRUE};

    public BooleanValueEditor(Object value) {
        super(options);
        this.setValue(value);
    }

    public void hideBorders() {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
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
        Boolean res = (this.getSelectedItem().equals(TRUE));
        return res;
    }

    @Override
    public void setValue(Object val) {
        Boolean boolValue = (Boolean) val;
        if ((boolValue != null) && (boolValue == true)) {
            this.setSelectedItem(TRUE);
        } else {
            this.setSelectedItem(FALSE);
        }
    }
}
