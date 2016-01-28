/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbprojects.maven;

import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.slingfs.local.LocalFileObject;
import org.jkan997.slingbeans.slingfs.local.LocalFileSystem;
import org.jkan997.slingbeans.vlt.VltManager;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author jakaniew
 */
public class ContentChangeListener implements FileChangeListener {

    public static Map<String, ContentChangeListener> listeners = new HashMap<String, ContentChangeListener>();

    public static void createListener(String contentPath, LocalSlingRootNode rootNode) {

        org.openide.filesystems.FileObject projectFo = FileUtil.toFileObject(new File(contentPath));
        ContentChangeListener oldListener = listeners.get(projectFo.getPath());
        if (oldListener != null) {
            projectFo.removeRecursiveListener(oldListener);
            listeners.remove(projectFo.getPath());
        }
        ContentChangeListener contentChangeListener = new ContentChangeListener(rootNode);
        projectFo.addRecursiveListener(contentChangeListener);
        listeners.put(projectFo.getPath(), contentChangeListener);

    }
    private final LocalFileSystem lfs;

    private final LocalSlingRootNode rootNode;

    private ContentChangeListener(LocalSlingRootNode rootNode) {
        this.rootNode = rootNode;
        this.lfs = rootNode.getFileSystem();
    }

    private Writer logWriter = null;

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

    @Override
    public void fileFolderCreated(FileEvent fe) {
        LogHelper.logInfo(this, "fileFolderCreated(%s)", fe.getFile().getPath());
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        LogHelper.logInfo(this, "fileDataCreated(%s)", fe.getFile().getPath());

    }

    @Override
    public void fileChanged(FileEvent fe) {
        String filePath = fe.getFile().getPath();
        if ((filePath.indexOf(".xml") > 0) || (filePath.indexOf("/.") > 0)) {
            return;
        }
        LogHelper.logInfo(this, "fileChanged(%s)", fe.getFile().getPath());
        if (lfs.isXmlDescriptorFile(new File(fe.getFile().getPath()))) {
            //rootNode.refresh();
            return;
        }
        FileObject fo = fe.getFile();
        LocalFileObject lfo = lfs.getFileObject(fo.getPath());

        try {
            Writer wrt = getOutputWriter();
            wrt.write("File changed: " + lfo.getFilePath() + "\n");

            SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
            String fsId = slingFsFactory.getDefualtFileSystemId();
            if (fsId == null) {
                wrt.write("No connection to Sling Server cannot save.\n");
                return;
            }
            FileSystem fs = slingFsFactory.getFileSystem(fsId);
            org.jkan997.slingbeans.slingfs.FileObject targetFo = fs.getFileObject(lfo.getFilePath());
            File localFile = new File(lfo.getLocalFilePath());
            if (localFile != null) {
                byte[] fileContent = IOHelper.readFileToBytes(localFile);
                if (targetFo != null) {
                    wrt.write("Updating file " + targetFo.getFilePath() + "\n");
                    targetFo.setFileContent(fileContent);
                } else {
                    String newFilePath = lfo.getFilePath();
                    String parentPath = newFilePath.substring(0, newFilePath.lastIndexOf("/"));
                    wrt.write("Trying to create new file in folder" + parentPath + "\n");
                    org.jkan997.slingbeans.slingfs.FileObject targetFoParent = fs.getFileObject(parentPath);
                    if (targetFoParent != null) {
                        fs.createFile(newFilePath, fileContent);
                    }
                }
            }
            /*VltManager vltManager = fs.getVltManager();
             vltManager.importContentToRemote(rootNode.getContentPath(),lfo.getFilePath());*/
            //rootNode.refresh();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        LogHelper.logInfo(this, "fileDeleted(%s)", fe.getFile().getPath());
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
        LogHelper.logInfo(this, "fileRenamed(%s)", fre.getFile().getPath());
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        LogHelper.logInfo(this, "fileAttributeChanged(%s)", fae.getFile().getPath());
    }

};
