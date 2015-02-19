package org.jkan997.slingbeans.nbprojects.maven;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Action;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbprojects.maven.actions.RefreshAction;
import org.jkan997.slingbeans.nbprojects.maven.actions.VltImportAction;
import org.jkan997.slingbeans.slingfs.local.LocalFileSystem;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Children;
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public class LocalSlingRootNode extends LocalAbstractNode {

    public static final String IMAGE = "org/jkan997/slingbeans/nbprojects/maven/sling.png";
    private Action[] actionArr = null;
    private RefreshAction refreshAction;
    private VltImportAction vltImportAction;
    private LocalFileSystem fileSystem;

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
            actionArr = actions.toArray(new Action[]{});
        }
        return actionArr;
    }

    @Override
    public String getDisplayName() {
        return "Sling Content";
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
        final Runnable loadWorkflowsTask = new Runnable() {
            @Override
            public void run() {
                refreshAsync();
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

}
