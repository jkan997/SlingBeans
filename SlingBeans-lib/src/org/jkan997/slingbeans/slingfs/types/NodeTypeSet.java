/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.slingfs.types;

import org.jkan997.slingbeans.slingfs.FileObject;
import java.util.Set;
import java.util.TreeSet;
import org.jkan997.slingbeans.helper.LogHelper;

/**
 *
 * @author jakaniew
 */
public class NodeTypeSet extends TreeSet<NodeType> {

    public final static Set<String> FILE_TYPES = new TreeSet<String>();
    public final static Set<String> FOLDER_TYPES = new TreeSet<String>();
    public final static String NT_FILE = "nt:file";
    public final static String NT_FOLDER = "nt:folder";
    public final static String SLING_FOLDER = "sling:Folder";
    public final static String SLING_ORDERED_FOLDER = "sling:OrderedFolder";
    public final static String AUTHORIZABLE_FOLDER = "rep:AuthorizableFolder";
    public final static String CQ_PAGE = "cq:Page";

    static {
        FILE_TYPES.add(NT_FILE);
        FOLDER_TYPES.add(NT_FOLDER);
        FOLDER_TYPES.add(SLING_FOLDER);
        FOLDER_TYPES.add(SLING_ORDERED_FOLDER);
        FOLDER_TYPES.add(AUTHORIZABLE_FOLDER);
    }

    public void init(FileObject nodeTypesFo) {
        for (FileObject ntFo : nodeTypesFo.getChildren()) {
            if (ntFo.getAttribute("jcr:isMixin").getBoolean() != true) {
                System.out.println(ntFo.getName());
                String ntName = ntFo.getName();
                NodeType nt = new NodeType(ntName);
                nt.setHasOrderableChildNodes((ntFo.getAttribute("jcr:hasOrderableChildNodes").getBoolean() == true));
                this.add(nt);
                if ("nt:file".equals(ntName)) {
                    LogHelper.logInfo(ntName, "");
                }
            }
        }
    }

    public NodeType getByName(String ntName) {
        for (NodeType nt : this) {
            if (nt.getName().equals(ntName)) {
                return nt;
            }
        }
        return null;
    }
}
