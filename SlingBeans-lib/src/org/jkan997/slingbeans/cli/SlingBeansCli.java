/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.cli;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileSystem;

/**
 *
 * @author jakaniew
 */
public class SlingBeansCli {

    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("user", true, "Remote user");
            options.addOption("password", true, "Remote user password");
            options.addOption("server", true, "Remote server");
            options.addOption("remote", true, "Remote folder");
            options.addOption("local", true, "Local file or files (; delimiter)");
            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            for (String a : cmd.getArgs()) {
                System.out.println(a);
            }
            for (Option o : cmd.getOptions()) {
                System.out.println(o + " " + o.getValue());
            }
            String user = cmd.getOptionValue("user");
            //System.out.println(user);
            String password = cmd.getOptionValue("password");
            String server = cmd.getOptionValue("server");
            String remote = cmd.getOptionValue("remote");
            String local = cmd.getOptionValue("local");
            System.out.println(server);
            LogHelper.disableLogs = false;
            FileSystem fs = new FileSystem(server, user, password);
            File localFile = new File(local);
            System.out.println(localFile.getAbsolutePath());
            String fileName = localFile.getName();
            FileInputStream fis = new FileInputStream(localFile);
            byte[] bytes = IOHelper.readInputStreamToBytes(fis);
            fis.close();
            String path = remote + "/" + fileName;
            FileObject fo = fs.getFileObject(path);
            if (fo==null){
                fs.createFile(path, bytes);
            } else {
                fo.setFileContent(bytes, true);
            }
            //System.out.println("FileObject "+fo.getPath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
