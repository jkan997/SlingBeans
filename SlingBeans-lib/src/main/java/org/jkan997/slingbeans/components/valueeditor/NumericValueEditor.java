/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components.valueeditor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author jkan997
 */
public abstract class NumericValueEditor extends JTextField implements ValueEditor {

    protected boolean floatAllowed = false;
    
    public NumericValueEditor(Object value) {
        setValue(value);
        this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)
                        || (c == KeyEvent.VK_ENTER) || (c == KeyEvent.VK_TAB)
                        || (Character.isDigit(c)))
                        || ((floatAllowed)&&(c=='.'))
                        ) {
                    e.consume();
                }
            }
        });
    }

  
}
