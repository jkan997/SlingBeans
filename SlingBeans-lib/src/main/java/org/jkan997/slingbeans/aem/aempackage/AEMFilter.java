/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jkan997.slingbeans.aem.aempackage;

/**
 *
 * @author jakaniew
 */
public class AEMFilter {
    private String mode;
    private String root;
    private String[] rules;

    public AEMFilter(String root) {
        this.root = root;
    }
    
    

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String[] getRules() {
        return rules;
    }

    public void setRules(String[] rules) {
        this.rules = rules;
    }
    
    public void toXML(StringBuilder sb){
        String res = String.format("<filter root=\"%s\"/>\n",root);
        sb.append(res);
    }
}
