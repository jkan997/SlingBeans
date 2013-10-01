/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

/**
 *
 * @author jkan997
 */

public class ServerUrlParser {


    public static ServerUrl parse(String s) {
        int ind, ind1;
        ind = s.indexOf("://");
        String protocol = s.substring(0, ind);
        String host = null;
        String portStr = null;
        int port;
        s = s.substring(ind + 3, s.length());
        ind = s.indexOf(":");
        if (ind > 0) {
            ind1 = s.indexOf("/");
            host = s.substring(0, ind);
            portStr = s.substring(ind + 1, ind1);
            port = Integer.parseInt(portStr);
        } else {
            ind1 = s.indexOf("/");
            host = s.substring(0, ind1);
            port = 8080;
        }
        protocol = protocol.toLowerCase();
        host = host.toLowerCase();
        ServerUrl res = new ServerUrl();
        res.protocol = protocol;
        res.host = host;
        res.port = port;
        return res;
    }
}
