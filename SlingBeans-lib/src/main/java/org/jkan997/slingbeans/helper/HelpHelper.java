/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

import java.awt.Desktop;
import java.net.URL;

/**
 *
 * @author jkan997
 */
public class HelpHelper {

    public static void openHelp(String topic) {
        try {
            String HELP_URL = "https://github.com/jkan997/SlingBeans/wiki/User-manual";
            Desktop.getDesktop().browse(new URL(HELP_URL).toURI());
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
