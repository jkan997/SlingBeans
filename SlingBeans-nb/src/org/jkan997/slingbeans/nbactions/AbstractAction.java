/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import java.io.IOException;
import java.io.Writer;
import static javax.swing.Action.NAME;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNode;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author jkan997
 */
public abstract class AbstractAction extends javax.swing.AbstractAction {

    protected String name;
    private Writer logWriter = null;
    protected LocalSlingNode localNode;


    public void setActionName(String val) {
        this.name = val;
        putValue(NAME, name);
    }

    public String getActionName() {
        return name;
    }

    public LocalSlingNode getLocalNode() {
        return localNode;
    }

    public void setLocalNode(LocalSlingNode localNode) {
        this.localNode = localNode;
    }
    
    
    private SlingNode getSlingNode() {
        return null;
    }

    protected Writer getOutputWriter() {
        if (logWriter == null) {

            IOProvider iop = IOProvider.getDefault();
            /*InputOutput io = iop.getIO("Sling Synchronization", false);
             if (io!=null){
             io.closeInputOutput();
             }*/
            InputOutput io = iop.getIO("Sling Synchronization", false);
            logWriter = io.getOut();
            io.setOutputVisible(true);
            io.select();
        }
        return logWriter;
    }
    

    protected void logInfo(String msg, Object... params) {
        Writer logWriter = getOutputWriter();
        String fmtMsg = String.format(msg, params);
        try {
            logWriter.append(fmtMsg);
            logWriter.append("\n");
        } catch (IOException iOException) {
        }
    }
    
    

    protected void logHeader(String msg, Object... params) {
        Writer logWriter = getOutputWriter();
        String fmtMsg = String.format(msg, params);

        try {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i <= fmtMsg.length(); i++) {
                line.append('-');
            }
            line.append('\n');
            logWriter.append(line);
            logWriter.append(fmtMsg);
            logWriter.append("\n");
            logWriter.append(line);

        } catch (IOException iOException) {
        }
    }
}
