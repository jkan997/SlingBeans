/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.sync;

import org.jkan997.slingbeans.sync.SyncAction;
import org.jkan997.slingbeans.sync.SyncEntry;
import org.jkan997.slingbeans.sync.SyncDescriptor;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jkan997
 */
public class SyncEntryTableModel extends AbstractTableModel {

    private SyncEntry[] syncEntries;
    private SimpleDateFormat hourSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final String titles[] = {"Path", "Local", "Remote", "Action"};

    @Override
    public String getColumnName(int column) {
        return titles[column];
    }

    public SyncEntry[] getSyncEntries() {
        return syncEntries;
    }

    public void setSyncEntries(SyncEntry[] syncEntries) {
        this.syncEntries = syncEntries;

    }

    @Override
    public int getRowCount() {
        return syncEntries.length;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    private String formatDate(long l) {
        String res = "not set";
        if (l > 0) {
            Date d = new Date(l);
            res = hourSdf.format(d);
        }
        return res;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SyncEntry se = syncEntries[rowIndex];
        if (columnIndex == 0) {
            return se.getPath();
        }
        if (columnIndex == 1) {
            return formatDate(se.getModifiedLocally());
        }
        if (columnIndex == 2)  {
            return formatDate(se.getModifiedRemote());
        }

        if (columnIndex == 3) {
            return se.getActionsStr();
            //return action!=null?action.toString():"NONE";
        }
        return "";
    }
}
