/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

/**
 *
 * @author jakaniew
 */
public class FileNameHelper {

    public static String[] getNameExt(String fileName) {
        int ind = fileName.lastIndexOf(".");
        String name = fileName;
        String ext = "";
        if (ind > 0) {
            name = fileName.substring(0, ind);
            ext = fileName.substring(ind+1, fileName.length());
        }
        return new String[]{name, ext};
    }
    
    public static void main(String[] args) {
        String fn = "alfa.txt.bat";
        String[] ne = getNameExt(fn);
        System.out.println(ne[0]);
        System.out.println(ne[1]);
    }
}
