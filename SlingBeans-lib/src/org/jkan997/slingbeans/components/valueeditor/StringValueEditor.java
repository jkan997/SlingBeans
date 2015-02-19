/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components.valueeditor;

import java.awt.BasicStroke;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.StrokeBorder;

/**
 *
 * @author jkan997
 */
public class StringValueEditor extends JTextField implements ValueEditor {

    public StringValueEditor(String text) {
        setValue(text);
        this.setMargin(new Insets(0,0,0,0));
    }
    
    @Override
    public void hideBorders(){
        this.setBorder(new StrokeBorder(new BasicStroke(1)));
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
