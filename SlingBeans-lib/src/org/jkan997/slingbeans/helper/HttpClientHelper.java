/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 *
 * @author jakaniew
 */
public class HttpClientHelper {

    private static ClientConnectionManager CLIENT_CONNECITON_MANAGER;

    static {
        try {
            /*TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] certificate, String authType) {
                    return true;
                }
            };
            SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", 443, sf));
            registry.register(new Scheme("https", 8443, sf));*/
            CLIENT_CONNECITON_MANAGER = new PoolingClientConnectionManager();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.logError(ex);
        }
    }

    public static DefaultHttpClient createHttpClient(String name, String password) {
        DefaultHttpClient httpClient = new DefaultHttpClient(CLIENT_CONNECITON_MANAGER);
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(name, password));
        return httpClient;
    }

    public static void main(String[] args) throws Exception {
        String urlOverHttps = "https://author203.adobedemo.com/projects.html";
        DefaultHttpClient httpClient = createHttpClient("admin", "admin");
        HttpGet getMethod = new HttpGet(urlOverHttps);
        HttpResponse response = httpClient.execute(getMethod);
        InputStream is = response.getEntity().getContent();
        String s = IOHelper.readInputStreamToString(is);
        System.out.println(s);
    }
}
