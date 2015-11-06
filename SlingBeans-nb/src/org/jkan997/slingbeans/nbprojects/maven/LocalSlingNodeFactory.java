package org.jkan997.slingbeans.nbprojects.maven;

import java.io.File;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.local.LocalFileSystem;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.util.RequestProcessor;

@NodeFactory.Registration(projectType = "org-netbeans-modules-maven")
public class LocalSlingNodeFactory implements NodeFactory {

    public static LocalSlingNodeFactory instance;
    private String contentPath;
    private static final RequestProcessor requestProcessor = new RequestProcessor(LocalSlingNodeFactory.class);

    public LocalSlingNodeFactory() {
        LocalSlingNodeFactory.instance = this;
    }

    public void export(String jcrPath) {
        ImportToRemoteThread irt = new ImportToRemoteThread(contentPath, jcrPath);
        requestProcessor.post(irt);

    }

    @Override
    public NodeList createNodes(Project project) {

        //Optionally, only return a new node
        //if some item is in the project's lookup:
        //MyCoolLookupItem item = project.getLookup().lookup(MyCoolLookupItem.class);
        //if (item != null) {
        try {

            LogHelper.logInfo(this, "Project path: " + project.getProjectDirectory().getPath());
            String projectPath = project.getProjectDirectory().getPath();
            contentPath = projectPath + "/src/main/content";
            LogHelper.logInfo(this, "Project content path: " + contentPath);
            if ((new File(contentPath)).exists()) {
                LogHelper.logInfo(this, "Project content path: exists");
                LocalFileSystem lfs = new LocalFileSystem();
                lfs.setContentPath(contentPath);
                LocalSlingRootNode rootNode = new LocalSlingRootNode(null);
                rootNode.setFileSystem(lfs);
                rootNode.initChildren();
                ContentChangeListener.createListener(contentPath, rootNode);
                return NodeFactorySupport.fixedNodeList(rootNode);
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
        //}

        //If the above try/catch fails, e.g.,
        //our item isn't in the lookup,
        //then return an empty list of nodes:
        return NodeFactorySupport.fixedNodeList();

    }

}
