/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 *
 * @author jakaniew
 */
public class HttpClientHelper {

    private static ClientConnectionManager CLIENT_CONNECITON_MANAGER = new PoolingClientConnectionManager();

    public static DefaultHttpClient createHttpClient(String name, String password) {
        DefaultHttpClient httpClient = new DefaultHttpClient(CLIENT_CONNECITON_MANAGER);
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(name, password));
        return httpClient;
    }
}
