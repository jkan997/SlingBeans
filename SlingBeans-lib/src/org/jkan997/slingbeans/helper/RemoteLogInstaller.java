/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;

/**
 *
 * @author jakaniew
 */
public class RemoteLogInstaller {

    private final FileSystem fs;

    public RemoteLogInstaller(FileSystem fs) {
        this.fs = fs;
    }

    public void installRemoteLog() throws IOException {
        DefaultHttpClient httpClient = HttpClientHelper.createHttpClient();
        String remoteLogUrl = "https://raw.githubusercontent.com/jkan997/SlingRemoteLog/master/Release/RemoteLog-bundle.jar";
        HttpGet get = new HttpGet(remoteLogUrl);
        HttpResponse response = httpClient.execute(get);
        HttpEntity resEntity = response.getEntity();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        IOHelper.readInputStreamToOutputStream(resEntity.getContent(), bos);
        get.releaseConnection();
        bos.close();
        LogHelper.logInfo(this, "Downloaded RemoteLog, size %d url %s", bos.size(), remoteLogUrl);
        /* FileOutputStream tmpFos = new FileOutputStream("/tmp/rl.zip");
        tmpFos.write(bos.toByteArray());
        tmpFos.close();
        if (1 == 1) {
            return;
        }*/
        FileObject remoteLogFo;
        FileObject remoteLogInstallFo;
        try {
            remoteLogFo = fs.getFileObject("/apps/remotelog");
            remoteLogFo.delete();
            System.out.println("Removing existing remote log.");
        } catch (Exception ex) {
            System.out.println("No existing remote log.");
        }

        FileObject appsFo = fs.getFileObject("/apps");
        appsFo.createNode("remotelog", "sling:Folder");
        fs.commmit();
        remoteLogFo = (FileObject) fs.getFileObject("/apps/remotelog");
        remoteLogFo.createNode("install", "nt:folder");
        fs.commmit();
        remoteLogInstallFo = (FileObject) fs.getFileObject("/apps/remotelog/install");

        remoteLogInstallFo.createFile("remotelog.jar", bos.toByteArray(), "application/java-archive");
        fs.commmit();
    }
}
