/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.io.File;
import org.jkan997.slingbeans.dialogs.SlingSyncConflictDialog;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbservices.SlingFsConnector;
import org.jkan997.slingbeans.slingfs.FileSystemConnector;
import org.jkan997.slingbeans.sync.SyncDescriptor;
import org.jkan997.slingbeans.sync.SyncMode;
import org.jkan997.slingbeans.sync.Synchronizer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Utilities;

@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.submenu.ProjectSubmenu")
@ActionRegistration(
        displayName = "Sling")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 111)
})
public class SynchronizeAction extends AbstractAction {

    protected final DataObject context;
    protected boolean alwaysShowDialog = false;
    protected Synchronizer sync;

    public SynchronizeAction(DataObject context) {
        this.setActionName("Synchronize fast");
        this.context = context;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fo = context.getPrimaryFile();
        File f = Utilities.toFile(fo.toURI());
        File syncedFolder = SyncDescriptor.findSyncDescriptor(f);
        FileSystemConnector fsc = new SlingFsConnector();
        logHeader("SYNCHRONIZE ACTION");
        logInfo("Synced folder: %s", syncedFolder.getPath());
        sync = new Synchronizer(fsc, syncedFolder);
        sync.setLogWriter(getOutputWriter());
        sync.check();
        if ((sync.hasConflict()) || (sync.hasRemovals()) || (alwaysShowDialog)) {
            SlingSyncConflictDialog sscd = new SlingSyncConflictDialog(null, true);
            sscd.setSync(sync);
            SwingHelper.showDialog(sscd);
        } else {
            sync.synchronize(SyncMode.MERGE);
        }
    }
}
