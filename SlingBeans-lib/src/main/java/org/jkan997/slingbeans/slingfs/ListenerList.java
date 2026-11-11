/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jkan997.slingbeans.slingfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that holds a list of listeners of some type.
 * Replacement of  EventListListener, that solves performance issue #20715
 * @author  rm111737
 */
class ListenerList<T> {
    private final List<T> listenerList;
    private List<T> copy = null;

    ListenerList() {
        listenerList = new ArrayList<T>();
    }

    /**
     * Adds the listener .
     **/
    public synchronized boolean add(T listener) {
        if (listener == null) {
            throw new NullPointerException();
        }

        copy = null;

        return listenerList.add(listener);
    }

    /**
     * Removes the listener .
     **/
    public synchronized boolean remove(T listener) {
        copy = null;

        return listenerList.remove(listener);
    }

    /**
     * Passes back the event listener list
     */
    public synchronized List<T> getAllListeners() {
        if (listenerList.isEmpty()) {
            return Collections.emptyList();
        }
        if (copy == null) {
            copy = new ArrayList<T>(listenerList);
        }
        return copy;
    }
    
    public synchronized boolean hasListeners() {
        return !listenerList.isEmpty();
    }

    static <T> List<T> allListeners(ListenerList<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getAllListeners();
    }
}
