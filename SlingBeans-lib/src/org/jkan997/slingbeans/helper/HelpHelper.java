/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.awt.Desktop;
import java.net.URL;

/**
 *
 * @author jakaniew
 */
public class HelpHelper {

    public static void openHelp(String topic) {
        try {
            String HELP_URL = "https://github.com/jkan997/SlingBeans/wiki/User-manual";
            String htmlFilePath = "path/to/html/file.html"; // path to your new file
            Desktop.getDesktop().browse(new URL(HELP_URL).toURI());
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
