/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jkan997.slingbeans.slingfs.FileSystem;

/**
 *
 * @author jakaniew
 */
public class VLTHelper {

    public static final String TMP_DIR = "/tmp/bose";

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    private static Map<String, File> childMap(File inputFile) {
        File[] files = inputFile.listFiles();
        Map<String, File> res = new LinkedHashMap<String, File>();
        for (File f : files) {
            res.put(f.getName(), f);
        }
        return res;

    }

    private static void copyDirFiles(File src, File dest) throws IOException {
     /*   if (!dest.exists()) dest.mkdir();
        Map<String, File> srcFiles = childMap(src);
        Map<String, File> destFiles = childMap(dest);
        for (Map.Entry<String, File> me : destFiles.entrySet()) {
            if (!srcFiles.containsKey(me.getKey())) {
                FileUtils.deleteDirectory(me.getValue());
            }
        }
        destFiles = childMap(dest);
        for (File srcFile : srcFiles.values()) {
            File destFile = new File(dest.getPath()+"/"+file.getName());
            if (srcFile.isDirectory()){
                copyDirFiles(srcFile,destFile);
            } else {
                
            }
        }
*/
    }

    public static String export(FileSystem fs, String remotePath, String localRootPath) throws Exception {
        String res;
        //FileUtils.deleteDirectory(new File(TMP_DIR));
        Runtime runtime = Runtime.getRuntime();
        List<String> cmds = new LinkedList<String>();
        /*cmds.add("/Users/jakaniew/java/vlt/bin/vlt2");
        cmds.add("-v");
        cmds.add("--credentials");
        cmds.add(String.format("\"%s:%s\"", fs.getName(), fs.getPassword()));
        cmds.add("export");
        cmds.add("\"" + fs.getServerPrefix() + "/crx" + "\"");
        cmds.add("\"" + remotePath + "\"");
        cmds.add("\"" + TMP_DIR + "\"");
                */
        
        cmds.add("/Users/jakaniew/svn/Adobe/AdobeCustomDemos/BOSE/export.sh");
        cmds.add(remotePath);
        String fullCmd = "";
        for (String c : cmds) {
            fullCmd += " " + c;
        }
                
        res = fullCmd + "\n\n";
                
        Process p = runtime.exec(cmds.toArray(new String[]{}));
        p.waitFor();
        res += IOHelper.readInputStreamToString(p.getInputStream());

        System.out.println(res);
        return res;

    }

}
