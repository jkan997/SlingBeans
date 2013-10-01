/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.components;

import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.jkan997.slingbeans.configuration.Host;

/**
 *
 * @author jkan997
 */
public class HostComboBoxModel implements ComboBoxModel {

    private Host[] hosts = null;
    private Host selectedItem = null;

    public HostComboBoxModel(Host[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (Host) anItem;
    }

    @Override
    public Host getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return hosts.length;
    }

    @Override
    public Object getElementAt(int index) {
        return hosts[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
