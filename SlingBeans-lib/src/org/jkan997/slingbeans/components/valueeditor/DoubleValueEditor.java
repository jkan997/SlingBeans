/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.Insets;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jkan997
 */
public class DoubleValueEditor extends NumericValueEditor {

    public DoubleValueEditor(Object value) {
        super(value);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.floatAllowed = true;
    }
    
     public void hideBorders(){
        this.setBorder(new EmptyBorder(0,0,0,0));
    }


    @Override
    public void init() {
    }

    @Override
    public boolean isValidValue() {
        return getDouble() != null;
    }

    private Double getDouble() {
        Double res = null;
        try {
            res = Double.parseDouble(this.getText());
        } catch (Exception ex) {
        }
        System.out.println(res);
        return res;
    }

    @Override
    public Double getValue() {
        Double res = getDouble();
        if (res == null) {
            res = 0.0;
        }
        return res;
    }

    @Override
    public void setValue(Object value) {
        if ((value == null) || (!(value instanceof Double))) {
            value = 0;
        }
        this.setText(value.toString());
    }
}
