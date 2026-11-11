/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.nbactions.property;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jkan997.slingbeans.dialogs.RemovePropertyDialog;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.SwingHelper;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbtree.SlingNode;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;


public class RemovePropertyAction extends AbstractAction {

    private SlingNode node;

    public RemovePropertyAction(SlingNode node) {
        setActionName("Remove property...");
        this.node = node;
    }

    private String[] getFileObjectProperties(){
        List<String> res = new ArrayList<String>();
        FileObject fo = node.getFileObject();
        Map<String, FileObjectAttribute> attrsMap = fo.getAttributesMap();
        for (Map.Entry<String,FileObjectAttribute> me : attrsMap.entrySet()){
            String name = me.getKey();
            FileObjectAttribute foa = me.getValue();
            if (!foa.isHidden()){
                res.add(name);
            }
        }
        Collections.sort(res);
        return res.toArray(new String[]{});
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        RemovePropertyDialog rpd = new RemovePropertyDialog(null,true);
        rpd.setProperties(getFileObjectProperties());
        SwingHelper.showDialog(rpd);
        if (rpd.isRemoveProperty()){
            String propName = rpd.getProperty();
            LogHelper.logInfo(this, "REMOVE "+propName);
            FileObject fo = node.getFileObject();
            FileObjectAttribute foa = fo.getAttribute(propName);
            LogHelper.logInfo(this, "FOA "+foa);
            foa.setRemoved(true);
            fo.saveAttributes();
        }
        
    }
}
