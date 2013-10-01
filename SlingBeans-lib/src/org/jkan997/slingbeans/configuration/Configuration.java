/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.configuration;

/**
 *
 * @author jkan997
 */
public interface Configuration {

    public static final String LAST_HOST = "last-host";
    public static final String HOSTS = "hosts";
    
    Object getObject(String name);

    void setObject(String name, Object value);

    String getSettingsDir();
}
