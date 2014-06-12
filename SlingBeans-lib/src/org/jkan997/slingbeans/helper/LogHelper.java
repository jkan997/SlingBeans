/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

//import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jkan997.slingbeans.configuration.Configuration;
import org.jkan997.slingbeans.configuration.ConfigurationImpl;

/**
 *
 * @author jkan997
 */
public class LogHelper {

    public static boolean disableLogs = false;
    public static boolean printToConsole = false;
    public final static String PREFIX;
    public final static String HTTP_PREFIX;

    static {
        
        Configuration configuration = ConfigurationImpl.getInstance();
        PREFIX = configuration.getSettingsDir() + "/logs";
        logInfo(LogHelper.class,"Log prefix "+PREFIX);
        File f = new File(PREFIX);
        if (!f.exists()) {
            f.mkdir();
        }
        HTTP_PREFIX = PREFIX+"/http";
        f = new File(HTTP_PREFIX);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    public static void logError(Exception ex) {
        if (disableLogs) return;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        appendFile("Exception: " + ex.getMessage() + "\n" + sw.toString(), "error");
        
    }

    public static void logInfo(Object sender, String msg) {
        if (disableLogs) return;
        logInfo(sender, msg, new String[]{});
    }

    public static void logInfo(Object sender, String msg, Object... args) {
        if (disableLogs) return;
        // if (sender instanceof FileObject) return;
        String fmtMsg = msg;
        String fileName = "out";
        if (args.length > 0) {
            fmtMsg = String.format(msg, args);
        }
        if (sender != null) {
            Class clazz = null;
            if (sender instanceof Class) {
                clazz = (Class) sender;
            } else {
                clazz = sender.getClass();
            }
            fileName = clazz.getSimpleName();
            fmtMsg = clazz.getSimpleName() + ": " + fmtMsg;
        }
        if (printToConsole) {
            System.out.println(fmtMsg);
        }
        appendFile(fmtMsg, fileName);
    }

    private synchronized static void appendFile(String msg, String name) {
        try {
            String filename = PREFIX + "/"+ name + ".log";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write(msg);
            fw.write("\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
