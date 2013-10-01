/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbservices;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jakaniew
 */
@ServiceProvider(service = ClassPathProvider.class)
public class SlingFsClassPathProvider implements ClassPathProvider {

    private Map<String, ClassPath> classPathMap = new HashMap<String, ClassPath>();

     private ClassPath createClassPath(String classpath) {
        List list = new ArrayList();

        File cpDir = new File(classpath);
        for (File cpFile : cpDir.listFiles()) {
            String cpFilePath = (cpFile.getPath());
            if (!cpFilePath.endsWith(".jar")) {
                continue;
            }
            LogHelper.logInfo(this, "File: " + cpFilePath);
            String item = cpFilePath;
            try {
                URL url;
                if (item.endsWith(".jar") || item.endsWith(".zip")) {
                    url = new URL("jar:" + new File(item).toURI().toURL().toExternalForm() + "!/");
                } else {
                    url = new File(item).toURI().toURL();
                }
                LogHelper.logInfo(this, url.toString());
                list.add(ClassPathSupport.createResource(url));
            } catch (MalformedURLException ex) {
                LogHelper.logError(ex);
            }
        }
        return ClassPathSupport.createClassPath(list);
    }
    
    @Override
    public ClassPath findClassPath(org.openide.filesystems.FileObject inputFo, String type) {
        ClassPath res = null;
        LogHelper.logInfo(this, "findClassPath(%s,%s) = %s", inputFo.getPath(), type, res);
        if ((inputFo instanceof FileObject) && (ClassPath.SOURCE.equals(type))) {

            FileObject fo = (FileObject) inputFo;
            FileObject cpParentFo = fo.getClassPathParent();
            if (cpParentFo != null) {
                res = classPathMap.get(cpParentFo.toURL().toString());
                if (res == null) {
                    res = ClassPathSupport.createClassPath(cpParentFo);
                    GlobalPathRegistry.getDefault().register(ClassPath.SOURCE, new ClassPath[]{res});
                    classPathMap.put(cpParentFo.toURL().toString(), res);
                }
            }


        }
        return res;
    }
}
