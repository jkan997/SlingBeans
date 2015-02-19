/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbserver;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jkan997.slingbeans.dialogs.host.SlingHostPanel;
import org.netbeans.spi.server.ServerInstanceImplementation;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public class SlingServerInstance implements ServerInstanceImplementation {

    private String instanceName = "SlingServer instance 1";
    private String serverName = "SlingServer";
    private boolean removable = true;
    private JPanel customizer;
    
    @Override
    public Node getFullNode() {
        return new AbstractNode(Children.LEAF) {
            @Override
            public String getDisplayName() {
                return instanceName;
            }
        };
    }

    @Override
    public Node getBasicNode() {
        return new AbstractNode(Children.LEAF) {
            @Override
            public String getDisplayName() {
                return instanceName;
            }
        };
    }

    @Override
    public JComponent getCustomizer() {
         synchronized (this) {
            if (customizer == null) {
                customizer = new SlingHostPanel();
            }
            return customizer;
        }
    }

    @Override
    public String getDisplayName() {
        return instanceName;
    }

    @Override
    public String getServerDisplayName() {
        return serverName;
    }

    @Override
    public boolean isRemovable() {
        return removable;
    }

    @Override
    public void remove() {
        //Here, remove the instance from the provider
    }
}
