/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.util.ArrayList;
import java.util.List;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingRootNode;
import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public class NbNodeHelper {

    public static Node getChildByName(Node node, String name) {
        Node[] nodes = node.getChildren().getNodes();
        Node res = null;
        for (Node n : nodes) {
            if (n.getDisplayName().equals(name)) {
                res = n;
                break;
            }
        }
        return res;
    }

    public static Node getChildByPath(Node node, String[] pathArr) {
        Node res = null;
        for (String pathPart : pathArr) {
            node = getChildByName(node, pathPart);
            if (node != null) {
                res = node;
            } else {
                break;
            }
        }
        return res;
    }

    public static String[] getLocalNodePath(Node node) {
        Node parent = node;
        final List<String> pathList = new ArrayList<String>();
        pathList.add(parent.getDisplayName());
        while ((parent = parent.getParentNode()) != null) {
            String name = parent.getDisplayName();
            if (LocalSlingRootNode.SLING_CONTENT.equals(name)) {
                break;
            }
            pathList.add(0, name);
        }
        return pathList.toArray(new String[]{});
    }

    public static LocalSlingRootNode findRootNode(Node node) {
        if (node instanceof LocalSlingRootNode) {
            return (LocalSlingRootNode) node;
        }
        while ((node = node.getParentNode()) != null) {
            if (node instanceof LocalSlingRootNode) {
                return (LocalSlingRootNode) node;
            }
        }
        return null;
    }
}
