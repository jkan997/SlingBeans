/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

import javax.swing.JCheckBox;

/**
 *
 * @author jakaniew
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
