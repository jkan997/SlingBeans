/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.loaders.DataObject;

public class BuildBundleAction extends SynchronizeAction {

    private SlingNode slingNode;

    public BuildBundleAction(DataObject context) {
        super(context);
        setActionName("Build bundle");
    }

    public SlingNode getSlingNode() {
        return slingNode;
    }

    public void setSlingNode(SlingNode slingNode) {
        this.slingNode = slingNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (slingNode != null) {
            buildTree(e);
        } else {
            buildCheckouted(e);
        }
    }

    public void buildTree(ActionEvent e) {
        try {
            FileObject bndFo = slingNode.getFileObject();
            FileSystem fs = (FileSystem) bndFo.getFileSystem();
            String bndDescriptorPath = bndFo.getPath();
            logInfo("Bundle bundle from tree, path: %s", bndDescriptorPath);
            buildBundle(fs, bndDescriptorPath);
        } catch (Exception ex) {
            LogHelper.logError(ex);
            throw new RuntimeException(ex);
        }
    }

    public void buildCheckouted(ActionEvent e) {
        try {
            super.actionPerformed(e);
            String bndDescriptorPath = sync.getDescriptor().getBundlePath();
            logInfo("Bundle bundle from checkout, path: %s", bndDescriptorPath);
            FileObject callerFo = sync.getRemoteRoot();
            FileSystem fs = (FileSystem) callerFo.getFileSystem();
            try {
                fs = (FileSystem) callerFo.getFileSystem();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            buildBundle(fs, bndDescriptorPath);
        } catch (Exception ex) {
            LogHelper.logError(ex);
            throw new RuntimeException(ex);
        }

    }

    private void cleanInstallDir(FileSystem fs, String installDir) {
        logInfo("Install dir: %s", installDir);
        try {
            FileObject installFo = fs.getFileObject(installDir);
            if (installFo != null) {
                String s = "";
                installFo.setChildrenLoaded(false);
                for (FileObject fo : installFo.getChildren()) {
                    s += fo.getNameExt() + " ,";
                    try {
                        fo.delete(null);
                    } catch (Exception ex) {
                        LogHelper.logError(ex);
                    }
                }
                logInfo("Files in install dir: %s", s);
            }
        } catch (Exception ex) {
            logInfo("Install dir does not exists.");
        }
    }

    private void buildBundle(FileSystem fs, String bndDescriptorPath) {
        logInfo("Bundle descriptor path: %s", bndDescriptorPath);
        String bndPath = bndDescriptorPath.substring(0, bndDescriptorPath.lastIndexOf("/"));
        logInfo("Bundle path: %s", bndPath);
        logInfo("File system: %s", fs);
        String installDir = bndPath + "/install";

        cleanInstallDir(fs, installDir);

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        };

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("bundleHome", "/" + bndPath);
        params.put("descriptor", "/" + bndDescriptorPath);
        byte[] bytes = fs.sendPost("/libs/crxde/build", null, params);
        String buildLog = new String(bytes);
        buildLog = buildLog.replaceAll("\\<[^>]*>", "");
        logHeader("BUILD LOG");
        logInfo(buildLog);

    }
}
