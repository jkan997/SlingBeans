/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.aem.aempackage;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jakaniew
 */
public class AEMPackage {

    private String name;
    private String description;
    private List<AEMFilter> filters = new LinkedList<AEMFilter>();

    public AEMPackage(String name) {
        this.name = name;
    }

    public List<AEMFilter> getFilters() {
        return filters;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    
    
    public void toXML(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<workspaceFilter version=\"1.0\">\n");
        for (AEMFilter filter : filters) {
            filter.toXML(sb);
        }
        sb.append("</workspaceFilter>");
    }
}
