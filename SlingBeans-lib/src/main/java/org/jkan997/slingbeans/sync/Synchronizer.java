/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.sync;

import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jkan997.slingbeans.helper.XMLHelper;
import org.jkan997.slingbeans.slingfs.FileSystemConnector;
import org.openide.util.Exceptions;

/**
 *
 * @author jkan997
 */
public class Synchronizer {

    public final static String SYNC_DESCRIPTOR = "sling_sync.xml";
    private FileObject remoteRoot;
    private Map<String, Object> fileRemoteChanges;
    private Map<String, Object> folderRemoteChanges;
    private File localRoot;
    SyncDescriptor descriptor = null;
    SyncDescriptor oldDescriptor = null;
    FileSystem fileSystem;
    private SimpleDateFormat hourSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private FileSystemConnector fsc = null;
    private Writer logWriter;

    public void setLogWriter(Writer logWriter) {
        this.logWriter = logWriter;
    }

    public void setLogOutputStream(OutputStream os) {
        this.logWriter = new OutputStreamWriter(os);
    }

    public Synchronizer(FileSystemConnector fsc, File localRoot) {
        this(fsc, null, null, null, localRoot);
    }

    public SyncDescriptor getDescriptor() {
        return descriptor;
    }

    public Synchronizer(FileSystemConnector fsc, String fileSystemId, String remotePath, String bndPath, File localRoot) {
        this.fsc = fsc;
        String syncDescFile = localRoot.getPath() + "/" + SYNC_DESCRIPTOR;
        File f = new File(syncDescFile);
        if (f.exists()) {
            descriptor = this.deserializeXMLToObject(syncDescFile);
            descriptor.clear();
            oldDescriptor = this.deserializeXMLToObject(syncDescFile);
        } else {
            oldDescriptor = new SyncDescriptor();
            descriptor = new SyncDescriptor();
            descriptor.setRemoteFsId(fileSystemId);
            descriptor.setRemotePath(remotePath);
            descriptor.setBundlePath(bndPath);
            logSyncInfo("Remote file system: %s, remote path: %s", fileSystemId, remotePath);
        }
        this.localRoot = localRoot;

    }

    public FileObject getRemoteRoot() {
        if (remoteRoot == null) {
            String remoteFsId = descriptor.getRemoteFsId();
            String remotePath = descriptor.getRemotePath();
            FileSystem fs = fsc.connectToFileSystem(remoteFsId);
            if (fs != null) {
                this.remoteRoot = fs.getFileObject(remotePath, 99);
            } else {
                logSyncInfo("Unable to connect filesystem %s", remoteFsId);
            }
        }
        return remoteRoot;
    }

    public long lastSync() {
        return oldDescriptor != null ? oldDescriptor.getLastSync() : 0;
    }

    public SyncDescriptor check() {
        FileObject remoteRoot = getRemoteRoot();
        System.out.println("LAST SYNC " + hourSdf.format(new Date(descriptor.getLastSync())));
        SyncScanner fos = new SyncScanner();
        fos.scan(remoteRoot, descriptor);
        fos.scan(localRoot, descriptor);
        SyncEntry[] entries = descriptor.entries();
        long lastSync = lastSync();
        for (SyncEntry se : entries) {

            if (!se.isFolder()) {
                if (se.getModifiedLocally() > lastSync) {
                    se.addAction(SyncAction.COPY_TO_REMOTE);
                }
                if (se.getModifiedRemote() > lastSync) {
                    se.addAction(SyncAction.COPY_TO_LOCAL);
                }

            } else {
                if (se.getModifiedLocally() == -1) {
                    se.addAction(SyncAction.CREATE_FOLDER_LOCAL);
                }
                if (se.getModifiedRemote() == -1) {
                    se.addAction(SyncAction.CREATE_FOLDER_REMOTE);
                }
            }



            if ((se.getModifiedLocally() == -1) && (se.getModifiedRemote() < lastSync)) {
                se.addAction(SyncAction.REMOVE_REMOTE);
            }

            if ((se.getModifiedRemote() == -1) && (se.getModifiedLocally() < lastSync)) {
                se.addAction(SyncAction.REMOVE_LOCAL);
            }



        }
        logSyncInfo("Checking for changes");
        return descriptor;
    }

    public boolean hasConflict() {
        SyncEntry[] entries = descriptor.entries();
        for (SyncEntry se : entries) {
            if (se.getActionCount() > 1) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRemovals() {
        SyncEntry[] entries = descriptor.entries();
        for (SyncEntry se : entries) {
            for (SyncAction action : se.getActions()) {
                if (SyncActionHelper.isRemovalAction(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeLocal(SyncEntry se) {
        try {
            String localPath = localRoot.getPath() + "/" + se.getPath();
            File f = new File(localPath);
            f.delete();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void removeRemote(SyncEntry se) {
        try {
            FileObject remoteRoot = getRemoteRoot();
            String remotePath = remoteRoot.getPath() + "/" + se.getPath();
            FileSystem.remove(remotePath, fileRemoteChanges);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void copyToLocal(SyncEntry se) {
        try {
            FileObject remoteRoot = getRemoteRoot();
            String remotePath = remoteRoot.getPath() + "/" + se.getPath();
            FileObject fo = remoteRoot.getFileSystem().getFileObject(remotePath);
            String localPath = localRoot.getPath() + "/" + se.getPath();
            InputStream is = fo.getInputStream();
            FileOutputStream fos = new FileOutputStream(localPath);
            IOHelper.readInputStreamToOutputStream(is, fos);
            fos.close();
            is.close();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void copyToRemote(SyncEntry se) {
        try {
            FileObject remoteRoot = getRemoteRoot();
            String localPath = localRoot.getPath() + "/" + se.getPath();
            InputStream is = new FileInputStream(localPath);
            byte[] content = IOHelper.readInputStreamToBytes(is);
            is.close();
            boolean createNew = (se.getModifiedRemote() <= 0);
            String remotePath = remoteRoot.getPath() + "/" + se.getPath();
            FileSystem.addFileContentChanges(remotePath, content, createNew, fileRemoteChanges);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void createFolderLocal(SyncEntry se) {
        try {
            String localPath = localRoot.getPath() + "/" + se.getPath();
            File folderFile = new File(localPath);
            folderFile.mkdir();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    private void createFolderRemote(SyncEntry se) {
        try {
            FileObject remoteRoot = getRemoteRoot();
            String remotePath = remoteRoot.getPath() + "/" + se.getPath();
            FileSystem.createFolder(remotePath, folderRemoteChanges);
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }

    public FileSystem getFileSystem() {
        try {
            FileObject remoteRoot = getRemoteRoot();
            return remoteRoot.getFileSystem();
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        return null;
    }

    public void synchronize(SyncMode mode) {
        if (mode == null) {
            mode = SyncMode.MERGE;
        }
        logSyncInfo("Sync mode %s", mode);
        SyncEntry[] entries = descriptor.entries();
        folderRemoteChanges = new LinkedHashMap<String, Object>();
        fileRemoteChanges = new LinkedHashMap<String, Object>();
        for (SyncEntry se : entries) {
            SyncAction action = se.getAction(mode);
            logSyncInfo("Checking entry %s, action: %s", se.getPath(), se.getActionsStr());
            if (action != null) {
                logSyncInfo("Executing action %s, file: %s", action, se.getPath());
                if (action == SyncAction.COPY_TO_LOCAL) {
                    copyToLocal(se);
                }
                if (action == SyncAction.CREATE_FOLDER_LOCAL) {
                    createFolderLocal(se);
                }
                if (action == SyncAction.COPY_TO_REMOTE) {
                    copyToRemote(se);
                }
                if (action == SyncAction.CREATE_FOLDER_REMOTE) {
                    createFolderRemote(se);
                }
                if (action == SyncAction.REMOVE_LOCAL) {
                    removeLocal(se);
                }
                if (action == SyncAction.REMOVE_REMOTE) {
                    removeRemote(se);
                }
            }
        }

        FileSystem fs = getFileSystem();
        for (Map.Entry<String, Object> me : folderRemoteChanges.entrySet()) {
            System.out.println(me.getKey() + "=" + me.getValue());
            logSyncInfo("Folder update %s=%s", me.getKey(), me.getValue());
        }
        fs.sendPost(folderRemoteChanges);
        for (Map.Entry<String, Object> me : fileRemoteChanges.entrySet()) {
            logSyncInfo("File update %s=%s", me.getKey(), me.getValue());
        }
        fs.sendPost(fileRemoteChanges);
        descriptor.setLastSync(System.currentTimeMillis());
        String syncDescFile = localRoot.getPath() + "/" + SYNC_DESCRIPTOR;
        serializeObjectToXML(syncDescFile, descriptor);
        /*  for (Map.Entry<String,Object> me : remoteChanges.entrySet()) {
         System.out.println(me.getKey()+"="+me.getValue());
         }*/
    }

    public void serializeObjectToXML(String xmlFileLocation, SyncDescriptor objectToSerialize) {
        FileWriter fw = null;
        LogHelper.logInfo(this, "Serializing to file " + xmlFileLocation);
        try {
            LogHelper.logInfo(this, "Serializing to file 1");

            XMLHelper xmlh = XMLHelper.getInstance();
            LogHelper.logInfo(this, "Serializing to file 1a");

            fw = new FileWriter(xmlFileLocation);
            LogHelper.logInfo(this, "Serializing to file 3");

            xmlh.serialize(objectToSerialize, fw);
            LogHelper.logInfo(this, "Serializing to file 4");

        } catch (Exception ex) {
            LogHelper.logError(ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                LogHelper.logError(ex);
            }
        }
    }

    /**
     * Reads Java Bean Object From XML File
     */
    public final SyncDescriptor deserializeXMLToObject(String xmlFileLocation) {
        FileReader fr = null;
        try {
            fr = new FileReader(xmlFileLocation);
            XMLHelper xmlh = XMLHelper.getInstance();
            SyncDescriptor res = (SyncDescriptor) xmlh.deserialize(fr);
            return res;
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return null;

    }

    private void logSyncInfo(String msg, Object... params) {
        LogHelper.logInfo(this, msg, params);
        if (logWriter != null) {
            String fmtMsg = String.format(msg, params);
            try {
                logWriter.append(fmtMsg);
                logWriter.append("\n");
            } catch (IOException iOException) {
            }
        }
    }
}
