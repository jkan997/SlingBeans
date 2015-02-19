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
public class VltPath {

    private String contentPath;
    private String jcrPath;

    public String getContentPath() {
        return contentPath;
    }

    public String getJcrPath() {
        return jcrPath;
    }

    private String trimPath(String s) {
        s = s.trim();
        if (s.endsWith("/")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public VltPath(String s) {
        final String JCR_ROOT = "jcr_root";
        int ind = s.lastIndexOf("/" + JCR_ROOT);
        String contentPath = s;
        String path = "/";
        if (ind > 0) {
            this.contentPath = trimPath(s.substring(0, ind));
            this.jcrPath = trimPath(s.substring(ind + JCR_ROOT.length() + 1, s.length()));
            if ("".equals(this.jcrPath)) {
                this.jcrPath = "/";
            }
        }

    }

    @Override
    public String toString() {
        return "VltPath{" + "contentPath=" + contentPath + ", jcrPath=" + jcrPath + '}';
    }

}
