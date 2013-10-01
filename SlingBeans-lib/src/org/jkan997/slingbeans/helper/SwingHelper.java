/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author jakaniew
 */
public class SwingHelper {

    public static void showDialog(JDialog d) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - d.getWidth()) / 2;
        final int y = (screenSize.height - d.getHeight()) / 2;
        d.setLocation(x, y);
        d.setVisible(true);
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
