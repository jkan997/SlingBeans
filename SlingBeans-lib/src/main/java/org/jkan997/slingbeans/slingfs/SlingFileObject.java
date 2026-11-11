/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author jakaniew
 */
public interface SlingFileObject {

    void setAttribute(String key, Object value) throws IOException;
    
    void setAttribute(String key, Object value, int type) throws IOException;

    Enumeration<String> getAttributes();

    public FileObjectAttribute getAttribute(String key);

    public Map<String, FileObjectAttribute> getAttributesMap();

    void saveAttributes();

    String getFilePath();
    
    String getLocalFilePath();

}
