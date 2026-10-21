package org.jkan997.slingbeans.nbprojects.maven;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbprojects.maven.actions.OpenFilterAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.RefreshAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.VltImportAction;
import org.jkan997.slingbeans.slingfs.local.LocalFileSystem;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.util.ImageUtilities;

public class LocalSlingRootNode extends LocalAbstractNode {

    public static final String IMAGE = "org/jkan997/slingbeans/nbprojects/maven/sling.png";
    private Action[] actionArr = null;
    private RefreshAction refreshAction;
    private VltImportAction vltImportAction;
    private LocalFileSystem fileSystem;
    private Project project;
    private OpenFilterAction openFilterAction;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalSlingRootNode(Children children) {
        super(children != null ? children : Children.LEAF);
        initActions();
        LogHelper.logInfo(this, "Local Root Node adding listener!");
        this.addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                LogHelper.logInfo(this, "Local Root Node Destroyed!");
            }
        });
    }

    private void initActions() {
        refreshAction = new RefreshAction(this);
        vltImportAction = new VltImportAction(this);
        openFilterAction = new OpenFilterAction(this);

    }

    public String getContentPath() {
        return fileSystem.getContentPath();
    }

    @Override
    public Action[] getActions(boolean popup) {
        if (actionArr == null) {
            List<Action> actions = new ArrayList<Action>();
            actions.add(refreshAction);
            actions.add(vltImportAction);
            actions.add(openFilterAction);
            actionArr = actions.toArray(new Action[]{});
        }
        return actionArr;
    }

    public final static String SLING_CONTENT = "Sling Content";

    @Override
    public String getDisplayName() {
        return SLING_CONTENT;
    }

    @Override
    public Image getIcon(int type) {
        DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
        LogHelper.logInfo(LocalSlingRootNode.class, "Project path " + root.getPrimaryFile().getPath());
        Image original = root.getNodeDelegate().getIcon(type);
        return ImageUtilities.loadImage(IMAGE);
    }

    @Override
    public Image getOpenedIcon(int type) {
        /*DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
         Image original = root.getNodeDelegate().getIcon(type);
         return ImageUtilities.mergeImages(original,
         ImageUtilities.loadImage(IMAGE), 7, 7);*/
        return getIcon(type);

    }

    public LocalFileSystem getLocalFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(LocalFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public void initChildren() {
        LocalSlingNodeChildren children = new LocalSlingNodeChildren(fileSystem.getRootFileObject());
        this.setChildren(children);

    }

    public void refresh() {
        refresh(null);
    }

    public void refresh(final Runnable uiActionAfterRefresh) {
        final Runnable loadWorkflowsTask = new Runnable() {
            @Override
            public void run() {
                refreshAsync();
                if (uiActionAfterRefresh != null) {
                    uiActionAfterRefresh.run();
                }
            }
        };
        ProgressUtils.runOffEventDispatchThread(loadWorkflowsTask, "Reading local FS", new AtomicBoolean(false), false);
    }

    public void refreshAsync() {
        fileSystem.refresh();
        initChildren();

    }

    public LocalFileSystem getFileSystem() {
        return fileSystem;
    }

    public Node getNodeByPath(String[] pathArr) {
        Node node = this;
        boolean foundNode = false;
        for (String pathPart : pathArr) {
            foundNode = false;
            Node[] nodes = node.getChildren().getNodes();
            for (Node n : nodes) {
                if (n.getDisplayName().equals(pathPart)) {
                    node = n;
                    foundNode = true;
                    break;
                }
            }
            if (!foundNode) {
                break;
            }
        }
        return node;
    }

}
