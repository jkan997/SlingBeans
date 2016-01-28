/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.nbprojects.maven.actions;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.NbNodeHelper;
import org.jkan997.slingbeans.nbprojects.maven.LocalAbstractNode;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingRootNode;
import org.jkan997.slingbeans.nbprojects.maven.MavenProjectUtils;
import org.openide.nodes.Node;

/**
 *
 * @author jkan997
 */
public class RefreshAction extends AbstractAction {

    public RefreshAction(LocalAbstractNode node) {
        this.node = node;
        setActionName("Refresh");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogHelper.logInfo(this, "Action %s", this.getClass().getName());
        final LocalSlingRootNode slingRootNode = this.getRootNode();
        Runnable uiActionAfterRefresh = new Runnable() {
            @Override
            public void run() {
                String[] pathArr = NbNodeHelper.getLocalNodePath(node);
                MavenProjectUtils.selectAndExpandNode(slingRootNode.getProject(), pathArr);
            }
        };
        slingRootNode.refresh(uiActionAfterRefresh);
    }
}
