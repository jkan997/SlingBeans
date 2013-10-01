/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.slingfs.types;

import java.util.Objects;

/**
 *
 * @author jkan997
 */
public class NodeType implements Comparable {
    private String name;
    private String primaryItemTime;
    private boolean hasOrderableChildNodes;

    public NodeType(String name) {
        this.name = name;
    }

    public String getPrimaryItemTime() {
        return primaryItemTime;
    }

    public void setPrimaryItemTime(String primaryItemTime) {
        this.primaryItemTime = primaryItemTime;
    }

    public boolean isHasOrderableChildNodes() {
        return hasOrderableChildNodes;
    }

    public void setHasOrderableChildNodes(boolean hasOrderableChildNodes) {
        this.hasOrderableChildNodes = hasOrderableChildNodes;
    }

    
    
    @Override
    public int compareTo(Object o) {
        NodeType nt2 = (NodeType)o;
        return name.compareTo(nt2.getName());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.name);
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
        final NodeType other = (NodeType) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
        
    
}
