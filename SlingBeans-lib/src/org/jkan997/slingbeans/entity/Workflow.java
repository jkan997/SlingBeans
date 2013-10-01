/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.entity;

/**
 *
 * @author jakaniew
 */
public class Workflow implements Comparable {
    private String name;
    private String path;
    private String description;

   
    public Workflow() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Workflow{" + "title=" + name + ", path=" + path + '}';
    }

    @Override
    public int compareTo(Object o) {
        Workflow w = (Workflow)o;
        return this.getName().compareTo(w.getName());
    }

    
    
}
