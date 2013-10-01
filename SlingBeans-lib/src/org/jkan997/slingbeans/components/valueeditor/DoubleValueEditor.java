/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

/**
 *
 * @author jakaniew
 */
public class DoubleValueEditor extends NumericValueEditor  {

    public DoubleValueEditor(Object value) {
        super(value);
        this.floatAllowed=true;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean isValidValue() {
        return getDouble()!=null;
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
        if ((value==null)||(!(value instanceof Double))){
            value = 0;
        }
        this.setText(value.toString());
    }
}
