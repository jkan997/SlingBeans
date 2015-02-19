/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbactions.remote;

import java.awt.event.ActionEvent;
import org.jkan997.slingbeans.nbactions.AbstractAction;
import org.jkan997.slingbeans.nbservices.SlingFsFactory;
import org.jkan997.slingbeans.slingfs.FileSystem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
/*
@ActionID(
        category = "SlingFs",
        id = "org.jkan997.slingbeans.nbactions.remote.PushAction")
@ActionRegistration(
        displayName = "SlingBeans Push")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-java/Actions", position = 111),
    @ActionReference(path = "Loaders/text/x-javascript/Actions", position = 111)

})
*/
public class PushAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        SlingFsFactory slingFsFactory = SlingFsFactory.lookup();
        FileSystem fs = slingFsFactory.getDefaultFileSystem();
        logInfo("RemotePush " + fs.getFileSystemId());
    }

}
