/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.swing.text.StyledDocument;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author jakaniew
 */
public class OpenEditorAction extends AbstractAction {

    private static Set<String> cpRegistrated = new HashSet<String>();
    private SlingNode node;

    public OpenEditorAction(SlingNode node) {
        setActionName("Open in editor...");
        this.node = node;
    }

    private synchronized void registerClasspath() throws MalformedURLException {
        if (1 == 2) {
            return;
        }
        // if (!cpRegistrated.contains("cq5")){
        File cq5libs = null;
        Set<URL> urls = new HashSet<URL>();
        StringBuilder classpathSb = new StringBuilder();
        for (File cq5lib : cq5libs.listFiles()) {
            if (cq5lib.getName().endsWith(".jar")) {
                try {
                    URL url = FileUtil.urlForArchiveOrDir(cq5lib);
                    urls.add(url);
                    /*  if (classpathSb.length()>0){
                     classpathSb.append(File.pathSeparator);
                     }
                     classpathSb.append(cq5lib.getCanonicalPath());*/
                } catch (Exception ex) {
                    LogHelper.logError(ex);
                }
            }
        }
        LogHelper.logInfo(this, classpathSb.toString());
        ClassPath cq5ClassPath = ClassPathSupport.createClassPath(urls.toArray(new URL[]{}));
        GlobalPathRegistry.getDefault().register(ClassPath.COMPILE, new ClassPath[]{cq5ClassPath});

        FileObject fo = node.getFileObject();
        FileObject cpParentFo = fo.getClassPathParent();
        LogHelper.logInfo(this, "CP Parent = %s", cpParentFo.getPath());
        ClassPath classPath = ClassPathSupport.createClassPath(cpParentFo);
        GlobalPathRegistry gpr = GlobalPathRegistry.getDefault();
        gpr.register(ClassPath.SOURCE, new ClassPath[]{classPath});
        final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        LogHelper.logInfo("Source path", "%s", sourcePath);
        StringBuilder sb = new StringBuilder();
        Set<org.openide.filesystems.FileObject> sourceRoots = gpr.getSourceRoots();
        sb.append("\n\n\nSource roots:\n");
        for (org.openide.filesystems.FileObject fo2 : sourceRoots) {
            sb.append(fo2.toURL().toString());
            sb.append("\n");
        }
        sb.append("\n\n\n");
        LogHelper.logInfo(this, sb.toString());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileObject fo = node.getFileObject();
            final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
            LogHelper.logInfo("Source path", "%s", sourcePath);
            DataObject d = DataObject.find(fo);

            System.out.println("Data object " + d);
            EditorCookie ec = (EditorCookie) d.getCookie(EditorCookie.class);
            if (ec != null) {
                ec.open();
                StyledDocument doc = ec.openDocument();
            } else {
                DataObject dob = DataObject.find(fo);
                Openable oc = dob.getLookup().lookup(Openable.class);
                if (oc != null) {
                    oc.open();
                }
            }
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
