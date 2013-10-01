/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.configuration;

import java.io.Serializable;
import java.util.Objects;
import org.jkan997.slingbeans.slingfs.FileSystem;

/**
 *
 * @author jakaniew
 */
public final class Host implements Serializable {

    private String hostId;
    private String hostUrl;
    private String user;
    private String password;

    public Host() {
    }

    public Host(String hostUrl, String user, String password) {
        setHostUrl(hostUrl);
        this.user = user;
        this.password = password;
    }

    
    
    
    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String host) {
        if (!host.endsWith("/")) {
            host += "/";
        }
        host = host.trim();
        this.hostUrl = host;

        this.hostId = FileSystem.generateFileSystemId(host);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostId() {
        return hostId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.hostId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Host other = (Host) obj;
        if (!Objects.equals(this.hostId, other.hostId)) {
            return false;
        }
        return true;
    }
}
