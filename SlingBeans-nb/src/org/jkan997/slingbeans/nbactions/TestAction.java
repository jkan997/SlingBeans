/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions;


import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.nbservices.SlingFsClassPathProvider;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collection;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.jkan997.slingbeans.helper.LogHelper;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author jakaniew
 */
public class TestAction extends AbstractAction {

    private SlingNode node;

    public TestAction(SlingNode node) {
        putValue(NAME, "TEST");
        this.node = node;
    }

    public void actionPerformed(ActionEvent e) {
        Lookup.Result<? extends ClassPathProvider> implementations =
                Lookup.getDefault().lookupResult(ClassPathProvider.class);
        Collection<? extends ClassPathProvider> allInstances = implementations.allInstances();
        for (Object o : allInstances) {
            LogHelper.logInfo(this, "ClassPathProvider %s", o);
        }
        SlingFsClassPathProvider cpp = Lookup.getDefault().lookup(SlingFsClassPathProvider.class);
        LogHelper.logInfo(this, "ClassPathProvider single %s", cpp);

    }

    public void xactionPerformed(ActionEvent e) {
        try {
            FileObject fo = node.getFileObject();
            URL foURL = fo.toURL();
            LogHelper.logInfo(this, "%s = %s", fo.getPath(), foURL);
            SlingFsFactory crxFsFactory = SlingFsFactory.lookup();
            FileObject fo2 = crxFsFactory.getFileObjects(foURL)[0];
            LogHelper.logInfo(this, "Test %s == %s [%s]", "" + fo.hashCode(), "" + fo2.hashCode(), "" + (fo == fo2));
        } catch (Exception ex) {
            LogHelper.logError(ex);
        }
    }
}
