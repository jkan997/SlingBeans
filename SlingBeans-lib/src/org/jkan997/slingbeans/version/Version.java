/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.version;

/**
 *
 * @author jakaniew
 */
public class Version {

    public static String ID = "id";
    public static String RELEASED = "released";
    public static String DOWNLOAD = "download";
    public static String CHANGES = "changes";

    private String id;
    private String released;
    private String download;
    private String changes;

    public Version() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNumericId() {
        if (id != null) {
            return VersionManager.versionToLong(id);
        } else {
            return 0;

        }
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getId() {
        return id;
    }

    public String getReleased() {
        return released;
    }

    public String getDownload() {
        return download;
    }

    public String getChanges() {
        return changes;
    }

    @Override
    public String toString() {
        return "Version{\n" + "id=" + id + " (" + this.getNumericId() + ")\n released=" + released + "\n download=" + download + "\n changes=" + changes + "\n}";
    }

}
