/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.helper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jakaniew
 */
public class ObjectHelper {

    public static boolean equalObjects(Object o1, Object o2) {
        if ((o1 == null) && (o2 == null)) {
            return true;
        }
        if ((o1 != null) && (o2 != null) && (o1.equals(o2))) {
            return true;
        }
        return false;
    }

    public static Map cloneMap(Map input) {
        Map res = new HashMap();
        for (Object meObj : input.entrySet()) {
            Map.Entry me = (Map.Entry) meObj;
            res.put(me.getKey(), me.getValue());
        }
        return res;
    }

    public static String toString(Object value, String defValue) {
        if (value == null) {
            return defValue;
        }
        return value.toString();
    }
}
