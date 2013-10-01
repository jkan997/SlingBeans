/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.Reader;
import java.io.Writer;
import org.jkan997.slingbeans.sync.SyncDescriptor;
import org.jkan997.slingbeans.sync.SyncEntry;

/**
 *
 * @author jakaniew
 */
public class XMLHelper {

    private static XMLHelper instance = null;
    private final XStream xstream;

    public static synchronized XMLHelper getInstance() {
        if (instance == null) {
            instance = new XMLHelper();
        }
        return instance;
    }

    public XMLHelper() {
        xstream = new XStream(new StaxDriver());
        xstream.alias("SyncDescriptor", SyncDescriptor.class);
        xstream.alias("SyncEntry", SyncEntry.class);
    }

    public void serialize(Object obj, Writer writer) {
        LogHelper.logInfo(this,"XML Serialization "+ obj);
        xstream.toXML(obj, writer);
    }

    public Object deserialize(Reader reader) {
        Object res = xstream.fromXML(reader);
        return res;
    }
}
