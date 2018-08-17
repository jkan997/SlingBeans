/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.cli;

import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileSystem;

/**
 *
 * @author jakaniew
 */
public class LogViewerCli {

    private final FileSystem fs;

    public LogViewerCli(FileSystem fs, String logName, String grep) {
        this.fs = fs;
      //  this.logName = logName;
      //  this.grep = grep;
    }

    public static void main(String[] args) {
        String url = "http://localhost:4502/";
        LogHelper.disableLogs = false;
        //  fs = new FileSystem(url, "jakaniew", "Pa$$word1!");
        FileSystem fs = new FileSystem(url, "admin", "admin");
        String logName="/logs/error.log";
    //    LogViewerCli cli = new LogViewerCli(fs);

    }
}
