/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs;

/**
 *
 * @author jakaniew
 */
public interface FileSystemConnector {
     FileSystem connectToFileSystem(String fsId);
}
