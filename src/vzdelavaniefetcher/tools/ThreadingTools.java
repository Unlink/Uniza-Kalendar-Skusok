/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher.tools;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Nástroje na prácu s vláknami
 * @author Unlink
 */
public class ThreadingTools {
    
    private static ExecutorService aThreadPool;
    
    /**
     * Vykoná objekt na runnable na ui vlákne
     * @param runnable
     * @param async 
     */
    public static final void runOnUIThread(Runnable runnable, boolean async) {
        if (async)
            java.awt.EventQueue.invokeLater(runnable);
        else
            try {
                java.awt.EventQueue.invokeAndWait(runnable);
        } catch (InterruptedException | InvocationTargetException ex) {
        }
    }
    
    /**
     * Vykonanie úlohy v thread poole
     * @param runnable 
     */
    public static synchronized final void runInThreadingPool(Runnable runnable) {
        if (aThreadPool == null) {
            aThreadPool = Executors.newCachedThreadPool();
        }
        aThreadPool.execute(runnable);
    }
    
    /**
     * Zrušenie thread poolu
     */
    public static synchronized final void shutdownThreadPool() {
        aThreadPool.shutdown();
        aThreadPool = null;
    }
}
