/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbservices;

import java.util.concurrent.atomic.AtomicBoolean;
import org.jkan997.slingbeans.dialogs.SlingHostDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.FileSystemConnector;
import org.netbeans.api.progress.ProgressUtils;

/**
 *
 * @author jkan997
 */
public class SlingFsConnector implements FileSystemConnector {

    public FileSystem connectToFileSystem(String fsId) {
        FileSystem fs = null;
        if (fsId != null) {
            SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
            fs = slingFsFactory.getFileSystem(fsId);
        }

        if (fs == null) {
            SlingHostDialog slingHostDialog = new SlingHostDialog(null, true);
            SwingHelper.showDialog(slingHostDialog);
            fs = slingHostDialog.getFileSystem();
            if (fs != null) {
                initSlingFs(fs);
            }
        }
        return fs;
    }

    private void initSlingFs(final FileSystem fs) {
        final Runnable initSlingFsTask = new Runnable() {
            @Override
            public void run() {
                try {
                    fs.readTypes();
                    SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
                    slingFsFactory.registerFileSystem(fs);
                    slingFsFactory.setDefualtFileSystemId(fs.getFileSystemId());
                    FileObject fo = fs.getFileObject("/");
                    rootNodeHandler(fo);
                    registerCp(fo);
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(initSlingFsTask, "Connect to Sling Repository", new AtomicBoolean(false), false);
    }

    protected void rootNodeHandler(FileObject fo) {
    }

   

    protected void registerCp(FileObject fo) {
      /*  LogHelper.logInfo(this, "trying to register classpath");
        try {
            ClassPath cp = createClassPath("/Volumes/MacData/jkan997/svn/CQ5-libs");
            ClassPath[] cps = new ClassPath[]{cp};
            LogHelper.logInfo(this, cp.toString());
           // LogHelper.logInfo(this, "A" + cp.findResource("/javax/jcr/Node.class").getName());
            GlobalPathRegistry.getDefault().register(ClassPath.SOURCE, cps);
        } catch (Exception ex) {
            LogHelper.logError(ex);
            LogHelper.logInfo(this, ex.getMessage());
        }*/
    }
}
