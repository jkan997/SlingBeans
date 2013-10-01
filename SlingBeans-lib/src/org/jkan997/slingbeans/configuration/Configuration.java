/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.configuration;

/**
 *
 * @author jakaniew
 */
public interface Configuration {

    public static final String LAST_HOST = "last-host";
    public static final String HOSTS = "hosts";
    
    Object getObject(String name);

    void setObject(String name, Object value);

    String getSettingsDir();
}
