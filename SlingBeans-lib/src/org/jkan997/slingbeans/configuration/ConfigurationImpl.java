/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.configuration;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jkan997
 */
public class ConfigurationImpl implements Configuration {

    private static ConfigurationImpl instance = null;
    private String settingsDir = null;

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new ConfigurationImpl();
        }
        return instance;
    }

    public ConfigurationImpl() {
        getSettingsDir();
    }

    public String getSettingsDir() {
        if (this.settingsDir == null) {
            String userHome = System.getProperty("user.home").replace('\\', '/');
            String settingsDirPath = userHome + "/.slingbeans";
            File settingsFile = new File(settingsDirPath);
            if (!settingsFile.exists()) {
                settingsFile.mkdir();
            }
            this.settingsDir = settingsFile.getAbsolutePath();
        }
        return this.settingsDir;
    }

    @Override
    public Object getObject(String name) {
        try {
            File file = new File(settingsDir + "/" + name + ".xml");
            if (!file.exists()){
                return null;
            }
            FileInputStream os = new FileInputStream(file);
            XMLDecoder decoder = new XMLDecoder(os);
            Object deserializedObject = decoder.readObject();
            decoder.close();
            return deserializedObject;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setObject(String name, Object value) {
        try {
            FileOutputStream os = new FileOutputStream(settingsDir + "/" + name + ".xml");
            XMLEncoder encoder = new XMLEncoder(os);
            encoder.writeObject(value);
            encoder.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        ConfigurationImpl ci = new ConfigurationImpl();
        Map<String,String> m = new HashMap<String,String>();
        //m.put("ss", "ddd");
        //ci.setObject("mapa", m);
        m = (Map<String, String>) ci.getObject("mapa");
        System.out.println(m);
    }
}
