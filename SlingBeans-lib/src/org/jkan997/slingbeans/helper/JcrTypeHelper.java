/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

import java.util.regex.Pattern;

/**
 *
 * @author jkan997
 */
public class JcrTypeHelper {

    public static final Pattern jsonDate = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}[-+]{1}[0-9]{2}[:]{0,1}[0-9]{2}(\\.){0,1}[0-9]{0,3}Z{0,1}$");

    public static int getType(String name, Object object) {
        if (object instanceof Double || object instanceof Float) {
            return PropertyType.DOUBLE;
        } else if (object instanceof Number) {
            return PropertyType.LONG;
        } else if (object instanceof Boolean) {
            return PropertyType.BOOLEAN;
        } else if (object instanceof String) {
            if (name != null) {
                if (name.startsWith(PropertyType.TYPENAME_REFERENCE)) {
                    return PropertyType.REFERENCE;
                }
                if (name.startsWith(PropertyType.TYPENAME_PATH)) {
                    return PropertyType.PATH;
                }
                if (name.startsWith(PropertyType.TYPENAME_NAME)) {
                    return PropertyType.NAME;
                }
                if (name.startsWith(PropertyType.TYPENAME_URI)) {
                    return PropertyType.URI;
                }
                if (name.startsWith(PropertyType.TYPENAME_DATE)) {
                    return PropertyType.DATE;
                }
            }
            if (jsonDate.matcher((String) object).matches()) {
                return PropertyType.DATE;
            }
            return PropertyType.STRING;
        }
        return PropertyType.UNDEFINED;
    }
}
