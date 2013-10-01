package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Writer;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.StringHelper;
import org.jkan997.slingbeans.dialogs.SlingCheckoutDialog;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbservices.SlingFsConnector;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.FileSystemConnector;
import org.jkan997.slingbeans.slingfs.FileSystemFactory;
import org.jkan997.slingbeans.sync.SyncDescriptor;
import org.jkan997.slingbeans.sync.Synchronizer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Utilities;


public class CheckoutAction extends AbstractAction {

    private SlingNode node;
    private final DataObject context;

    public CheckoutAction(DataObject context) {
        this.setActionName("Sling Checkout...");
        this.context = context;
    }

    public void setNode(SlingNode node) {
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File localFile = null;
        try {
            FileObject fo = context.getPrimaryFile();
            localFile = Utilities.toFile(fo.toURI());

        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        File syncDescFile = SyncDescriptor.findSyncDescriptor(localFile);
        if (syncDescFile != null) {
            SwingHelper.showMessage("Tree already checked out");
            return;
        }
        SlingCheckoutDialog scd = new SlingCheckoutDialog(null, true);
        SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
        String fsId = slingFsFactory.getDefualtFileSystemId();
        scd.setLocalPath(localFile.getAbsolutePath());

        if (fsId == null) {
            SwingHelper.showMessage("Please connect to Sling before checkout");
            return;
        }



        FileSystem fs = slingFsFactory.getFileSystem(fsId);
        scd.setRemoteHost(fs.getFileSystemId());

        SwingHelper.showDialog(scd);
        if (scd.isCheckout()) {
            String jcrPath = scd.getFolderPath();
            jcrPath = StringHelper.normalizePath(jcrPath);

            File localFolder = new File(scd.getLocalPath());
            if (!localFolder.exists()) {
                localFolder.mkdir();
            }
            FileSystemFactory fsf = FileSystemFactory.getInstance();

            Writer logWriter = this.getOutputWriter();
            logHeader("CHECKOUT ACTION");
            FileSystemConnector fsc = new SlingFsConnector();
            String bndPath = scd.getBndPath();
            if ((bndPath!=null)&&(bndPath.startsWith("/"))) bndPath=bndPath.substring(1);
            Synchronizer sync = new Synchronizer(fsc, fsId, jcrPath, bndPath, localFolder);
            sync.setLogWriter(logWriter);
            SyncDescriptor syncEntryMap = sync.check();
            sync.synchronize(null);
        }
    }

    public void actionPerformedx(ActionEvent e) {
        /*  OpenProjects openProjects = OpenProjects.getDefault();
         Project[] projects = openProjects.getOpenProjects();
         for (Project p : projects) {
         LogHelper.logInfo(this, p.toString());
         LogHelper.logInfo(this, p.getProjectDirectory().toURL().toString());
         }
         IOProvider iop = IOProvider.getDefault();
         InputOutput io = iop.getIO("Checkout", true);
         io.getOut().append("Check out ! LALALAL");*/
    }
}
