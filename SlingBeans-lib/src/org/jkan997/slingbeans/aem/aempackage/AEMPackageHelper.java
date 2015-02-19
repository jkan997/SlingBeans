package org.jkan997.slingbeans.aem.aempackage;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.openide.filesystems.FileStateInvalidException;

/**
 *
 * @author jakaniew
 */
public class AEMPackageHelper {
    
    private static AEMPackage processPackage(FileObject pkgFo) throws FileStateInvalidException{
        String pkgName = pkgFo.getName().replace(".zip", "");
        AEMPackage res  = new AEMPackage(pkgName);
        try{
        FileObject filterFo = pkgFo.getFileSystem().getFileObject(pkgFo.getPath()+"/jcr:content/vlt:definition/filter");
        System.out.println(filterFo);
        FileObject[] filters = filterFo.getChildren();
        for (FileObject filter : filters){
            String root = filter.getAttribute("root").getValue().toString();
            res.getFilters().add(new AEMFilter(root));
        }
        return res;
        } catch (Exception ex){};
        return null;
    }
    
    public static List<AEMPackage> processPackageFo(FileObject packagesFo) throws Exception{
        List<AEMPackage>  res = new ArrayList<AEMPackage>();
          for (FileObject groupFo : packagesFo.getChildren()){
            if (groupFo.isFolder()){
                String groupName = groupFo.getName();
                if (groupName.equalsIgnoreCase("day")){
                    continue;
                }
                if (groupName.equalsIgnoreCase("adobe")){
                    continue;
                }
                for (FileObject pkgFo : groupFo.getChildren()){
                    System.out.println(groupFo.getPath()+""+pkgFo.getName());
                    AEMPackage pkg = processPackage(pkgFo);
                    if (pkg!=null){
                        res.add(pkg);
                        if (pkg.getName().contains("navy")){
                            FileWriter fw = new FileWriter("/Volumes/MacData/jakaniew/git/Adobe/Custom-Demos/navy-dam/"+pkg.getName()+".xml");
                            StringBuilder sb = new StringBuilder();
                            pkg.toXML(sb);
                            fw.append(sb);
                            fw.close();
                        }
                    }
                }
            }
        }
          
          
          return res;
    }
}
