/**
 * SlingBeans - NetBeans Sling plugin
 * https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.helper;

import java.util.TimerTask;

/**
 *
 * @author jkan997
 */
public abstract class DisposableTimerTask extends TimerTask implements Disposable {
    
    @Override
    public void dispose() {
        LogHelper.logInfo(this,"Disposing Timer Task");
        this.cancel();
    }
}
