/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jkan997.slingbeans.nbprojects.maven;

import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.jkan997.slingbeans.vlt.VltManager;

/**
 *
 * @author jakaniew
 */
public class ImportToRemoteThread implements Runnable {

    private String contentPath;
    private String jcrPath;

    public ImportToRemoteThread(String contentPath, String jcrPath) {
        this.contentPath = contentPath;
        this.jcrPath = jcrPath;
    }
    
    
    
    @Override
    public void run() {

        try {
            SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
            String fsId = slingFsFactory.getDefualtFileSystemId();
            if (fsId != null) {
                FileSystem fs = slingFsFactory.getFileSystem(fsId);
                VltManager vltManager = fs.getVltManager();
                vltManager.importContentToRemote(contentPath, jcrPath);
            } else {
                LogHelper.logInfo(this, "No Sling Server connection.");
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }    }
    
    
}
