/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.PropertyType;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author jakaniew
 */
public class ValueEditorContainer extends JPanel{
    private Object value;
    private ValueEditor ve;
    private int type;
    
    
    public ValueEditorContainer(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public ValueEditorContainer(LayoutManager layout) {
        super(layout);
    }

    public ValueEditorContainer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public ValueEditorContainer() {
    }

    public boolean isValidValue(){
        boolean res = ve.isValidValue();
        if (res){
            this.setBackground(new Color(Color.OPAQUE));
        } else {
            this.setBackground(Color.RED);
        }
        return res;
    }
    
    public Object getValue(){
        return ve.getValue();
    }
    
    public int getType(){
        return type;
    }
    
      public void setTypeAndValue(String typeName,Object value) {
          int type = PropertyType.valueFromName(typeName);
          System.out.println("TYPE: " + type);
          setTypeAndValue(type,value);
      }
      
      private Long LONG_ZERO = new Long(0);
      
      private Long toLong(Object obj){
          if (obj==null) return LONG_ZERO;
          if (obj instanceof Long) return (Long)obj;
          if (obj instanceof Integer) return ((Integer)obj).longValue();
          return LONG_ZERO;
      }
    
    public void setTypeAndValue(int type,Object value) {
        this.setLayout(new GridLayout());
        System.out.println(type);
        ValueEditor ve = null;
        if (type==PropertyType.STRING){
            if (value==null) value="";
            ve = new StringValueEditor(value.toString());
        }
        if (type==PropertyType.BOOLEAN){
            if (value==null) value=true;
            ve = new BooleanValueEditor((Boolean)value);
        }
         if (type==PropertyType.DOUBLE){
             if (value==null) value=0.0;
            ve = new DoubleValueEditor((Double)value);
        }
          if (type==PropertyType.LONG){
            ve = new LongValueEditor(toLong(value));
        }
        if (ve!=null){
            if (this.getComponentCount()>0){
                this.remove(0);
            }
            //this.invalidate();
            System.out.println("X"+ve);
            JComponent comp = (JComponent)ve;
            comp.setVisible(true);
            //comp.setSize(400, 50);
            LogHelper.logInfo(this, "Add component",ve);
            this.add((JComponent)ve);
             this.ve=ve;
            this.type=type;
            this.revalidate();
        }
    }
    
    
}
