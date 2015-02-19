/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.Insets;
import javax.swing.border.EmptyBorder;

public class LongValueEditor extends NumericValueEditor {

    public LongValueEditor(Object value) {
        super(value);
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
        return getLong() != null;
    }

    private Long getLong() {
        Long res = null;
        try {
            res = Long.parseLong(this.getText());
        } catch (Exception ex) {
        };
        return res;
    }

    @Override
    public Long getValue() {
        Long res = getLong();
        return res != null ? res : 0;
    }

    @Override
    public final void setValue(Object value) {
        if ((value == null) || (!(value instanceof Long))) {
            value = 0;
        }
        this.setText(value.toString());
    }
}
