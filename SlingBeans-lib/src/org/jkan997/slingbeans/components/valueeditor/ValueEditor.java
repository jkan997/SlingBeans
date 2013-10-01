/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

/**
 *
 * @author jakaniew
 */
public interface ValueEditor {

    public void init();
    
    public boolean isValidValue();

    public Object getValue();

    public void setValue(Object val);
}
