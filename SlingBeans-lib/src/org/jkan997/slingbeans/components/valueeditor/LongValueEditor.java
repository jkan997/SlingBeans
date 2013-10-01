/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

public class LongValueEditor extends NumericValueEditor {

    public LongValueEditor(Object value) {
        super(value);
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
