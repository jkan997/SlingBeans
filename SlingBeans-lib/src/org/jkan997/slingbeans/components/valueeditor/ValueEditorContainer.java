/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.components.valueeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.PropertyType;

/**
 *
 * @author jkan997
 */
public class ValueEditorContainer extends JPanel {

    private Object value;
    private ValueEditor ve;
    private int type;
    private boolean hideBorders;

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

    public boolean isValidValue() {
        boolean res = ve.isValidValue();
        if (res) {
            this.setBackground(new Color(Color.OPAQUE));
        } else {
            this.setBackground(Color.RED);
        }
        return res;
    }

    public Object getValue() {
        return ve.getValue();
    }

    public int getType() {
        return type;
    }

    public void setTypeAndValue(String typeName, Object value) {
        int type = PropertyType.valueFromName(typeName);
        System.out.println("TYPE: " + type);
        setTypeAndValue(type, value);
    }

    private Long LONG_ZERO = new Long(0);

    private Long toLong(Object obj) {
        if (obj == null) {
            return LONG_ZERO;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        return LONG_ZERO;
    }

    public void hideBorders() {
        this.hideBorders = true;
    }

    public void setTypeAndValue(int type, Object value) {
        LogHelper.logInfo(this,"Type: "+type+" value "+value);
        this.setLayout(new GridLayout());
        System.out.println(type);
        ValueEditor ve = null;
        if (type == PropertyType.STRING) {
            if (value == null) {
                value = "";
            }
            ve = new StringValueEditor(value.toString());
        }
        if (type == PropertyType.BOOLEAN) {
            if (value == null) {
                value = true;
            }
            ve = new BooleanValueEditor((Boolean) value);
        }
        if (type == PropertyType.DOUBLE) {
            if (value == null) {
                value = 0.0;
            }
            ve = new DoubleValueEditor((Double) value);
        }
        if (type == PropertyType.LONG) {
            ve = new LongValueEditor(toLong(value));
        }
        if (ve != null) {
            LogHelper.logInfo(this,"VE Class: "+ve.getClass().getSimpleName());
            if (this.getComponentCount() > 0) {
                this.remove(0);
            }
            //this.invalidate();
            System.out.println("X" + ve);
            JComponent comp = (JComponent) ve;
            comp.setVisible(true);
            if (hideBorders) {
                GridLayout bd = new GridLayout();
                bd.setHgap(0);
                bd.setVgap(0);
                bd.setRows(1);
                bd.setColumns(1);
                this.setLayout(bd);
                ve.hideBorders();
            }
            this.setOpaque(true);
            //comp.setSize(400, 50);
            LogHelper.logInfo(this, "Add component ", ve);

            this.add(comp);
            this.ve = ve;
            this.type = type;
            this.revalidate();
        }
    }

}
