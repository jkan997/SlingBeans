/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.helper;

/**
 *
 * @author jkan997
 */
public class StringHelper {

    public static String getParentPath(String s) {
        int ind = s.lastIndexOf("/");
        if (ind > 0) {
            return s.substring(0, ind);
        } else {
            if (s.length() > 0) {
                return "";
            } else {
                return null;
            }
        }
    }

    public static String[] extractFsFromPath(String fullPath) {
        int counter = 0;
        String fsId = fullPath;
        String path = "";;
        for (int i = 0, len = fullPath.length(); i < len; i++) {
            char c = fullPath.charAt(i);
            if (c == '/') {
                counter++;
            }
            if (counter == 6) {
                fsId = fullPath.substring(0, i);
                if (i < fullPath.length()-1) {
                    path = fullPath.substring(i+1, fullPath.length());
                }
                break;
            }
        }
        fsId=fsId.trim();
        path = normalizePath(path);
        return new String[]{fsId, path};
    }

    public static String[] extractNameExt(String s) {
        int ind1 = s.lastIndexOf("/");
        if (ind1 < 0) {
            ind1 = 0;
        } else {
            ind1 = ind1 + 1;
        }
        int ind2 = s.lastIndexOf(".");
        String name = "";
        String ext = "";
        if (ind2 < ind1) {
            ind2 = -1;
        }
        if (ind2 > 0) {
            name = s.substring(ind1, ind2);
            ext = s.substring(ind2 + 1, s.length());
        } else {
            name = s.substring(ind1);
        }
        return new String[]{name, ext};
    }

    public static String normalizePath(String path) {
        path = path.trim();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
    
    public static String objectToString(Object o, String def){
        if (o!=null){
            return o.toString();
        }
        return def;
    }

    public static void main(String[] args) {
        String s = "  /alfa/beta.org/dsds/ ";
        //String[] arr = extractNameExt(s);
        //System.out.println(String.format("%s@%s", arr[0], arr[1]));
        // System.out.println(arr[0]+" "+arr[1]);
        //  String pp = getParentPath(s);
        //  System.out.println(pp);
        s = normalizePath(s);
        System.out.println(s);
    }
}
