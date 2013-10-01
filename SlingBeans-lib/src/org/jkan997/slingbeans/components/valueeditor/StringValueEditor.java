/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

import javax.swing.JTextField;

/**
 *
 * @author jakaniew
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
