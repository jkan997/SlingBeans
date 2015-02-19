/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jkan997
 */
public class BooleanValueEditor extends JCheckBox implements ValueEditor {

    public BooleanValueEditor(Object value) {
        this.setValue(value);
        this.setMargin(new Insets(0, 0, 0, 0));

    }
    
     public void hideBorders(){
        this.setBorder(new EmptyBorder(0,0,0,0));
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
        this.setSelected((Boolean) val);
    }
}
