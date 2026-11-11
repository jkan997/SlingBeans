/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

/**
 *
 * @author jakaniew
 */
public class MimeTypeHelper {

    public final static String JSON = "application/json";
    public final static String BINARY = "application/octet-stream";
    public final static String TEXT = "text/plain";

    public final static boolean isTextMime(String mimeType) {
        mimeType = mimeType.toLowerCase().trim();
        if (mimeType.equals(JSON)) {
            return true;
        }
        if (mimeType.startsWith("text/")) {
            return true;
        }
        return false;
    }
}
