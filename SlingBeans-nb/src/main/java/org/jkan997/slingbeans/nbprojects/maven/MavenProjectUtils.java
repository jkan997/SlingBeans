package org.jkan997.slingbeans.nbprojects.maven;

import java.io.File;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.NbNodeHelper;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ProjectUtils;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Class to handle various aspects of NetBeans Project management.
 *
 * @author alexbcoles
 */
public class MavenProjectUtils {

    private static final String ProjectTab_ID_LOGICAL = "projectTabLogical_tc"; // NOI18N

    public static void selectAndExpandNode(final Project p, final String[] path) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    ExplorerManager.Provider ptLogial = findDefault(ProjectTab_ID_LOGICAL);
                    ExplorerManager em = ptLogial.getExplorerManager();
                    Node rootNode = em.getRootContext();
                    String projectName = ProjectUtils.getInformation(p).getDisplayName();
                    Node projectNode = NbNodeHelper.getChildByName(rootNode, projectName);
                    if (projectNode != null) {
                        Node slingContentNode = NbNodeHelper.getChildByName(projectNode, LocalSlingRootNode.SLING_CONTENT);
                        if (slingContentNode != null) {
                            Node nodeToExpand = NbNodeHelper.getChildByPath(slingContentNode, path);
                            ptLogial.getExplorerManager().setExploredContext(nodeToExpand, new Node[]{nodeToExpand});

                        }
                    }
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }

        }
        );
    }

    public static String getProjectName(final File root) {
        if (root == null || !root.isDirectory()) {
            return null;
        }
        final ProjectManager projectManager = ProjectManager.getDefault();
        FileObject rootFileObj = FileUtil.toFileObject(FileUtil.normalizeFile(root));
        // This can happen if the root is "ssh://<something>"
        if (rootFileObj == null || projectManager == null) {
            return null;
        }
        String res = null;
        if (projectManager.isProject(rootFileObj)) {
            try {
                Project prj = projectManager.findProject(rootFileObj);

                res = getProjectName(prj);
            } catch (Exception ex) {
                LogHelper.logError(ex);
            }
        }
        return res;
    }

    public static String getProjectName(final Project p) {
        if (p == null) {
            return null;
        }
        ProjectInformation pi = ProjectUtils.getInformation(p);
        return pi == null ? null : pi.getDisplayName();
    }

    private static synchronized ExplorerManager.Provider findDefault(String tcID) {
        TopComponent tc = WindowManager.getDefault().findTopComponent(tcID);
        return (ExplorerManager.Provider) tc;
    }

}
