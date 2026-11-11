/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jkan997
 */
public class ComboBoxSet {

    private final Set sourceSet;
    private final boolean sort;
    private String[] stringArray;
    private Map<String,Object> labelMap = new HashMap<String,Object>();

    public ComboBoxSet(Set sourceSet, boolean sort) {
        this.sourceSet = sourceSet;
        this.sort = sort;
        getStringArray();
    }

    public ComboBoxSet(Set sourceSet) {
        this.sourceSet = sourceSet;
        this.sort = false;
        getStringArray();
    }

    public String getFirstString() {
        String[] elements = getStringArray();
        if (elements.length > 0) {
            return elements[0];
        }
        return null;
    }
    
    public Object getByLabel(String label){
        return labelMap.get(label);
    }

    public String[] getStringArray() {
        if (stringArray == null) {
            String label = null;;
            String[] res = new String[sourceSet.size()];
            int i = 0;
            for (Object o : sourceSet) {
                label = convertToString(o);
                res[i] = label;
                labelMap.put(label, o);
                i++;
            }
            if (sort) {
                Arrays.sort(res);
            }
            stringArray = res;
        }
        return stringArray;
    }

    protected String convertToString(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    public Object getValue(String val) {
        for (Object o : sourceSet) {
            if (o.toString().equals(val)) {
                return val;
            }
        }
        return null;
    }

    public Set getSourceSet() {
        return sourceSet;
    }
}
