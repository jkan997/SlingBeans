/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions;

import java.io.IOException;
import java.io.Writer;
import static javax.swing.Action.NAME;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author jakaniew
 */
public abstract class AbstractAction extends javax.swing.AbstractAction {

    protected String name;
    private Writer logWriter = null;

    public void setActionName(String val) {
        this.name = val;
        putValue(NAME, name);
    }

    public String getActionName() {
        return name;
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
