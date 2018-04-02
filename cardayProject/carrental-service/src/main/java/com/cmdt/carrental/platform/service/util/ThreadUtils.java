package com.cmdt.carrental.platform.service.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class ThreadUtils {

    /**
     * Wrapper over ScheduledThreadPoolExecutor.
     */
     public static ScheduledExecutorService newDaemonThreadScheduledExecutor(String threadName) {
         ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
         ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8, threadFactory);
        // By default, a cancelled task is not automatically removed from the work queue until its delay
        // elapses. We have to enable it manually.
         executor.setRemoveOnCancelPolicy(true);
         return executor;
    }

    /**
     * Create a thread factory that names threads with a prefix and also sets the threads to daemon.
     */
    public static ThreadFactory namedThreadFactory(String prefix) {
        return new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build();
    }

    /**
     * Wrapper over newCachedThreadPool. Thread names are formatted as prefix-ID, where ID is a
     * unique, sequentially assigned integer.
     */

     public static ThreadPoolExecutor newDaemonCachedThreadPool(String prefix) {
         ThreadFactory threadFactory = namedThreadFactory(prefix);
         return (ThreadPoolExecutor) Executors.newCachedThreadPool(threadFactory);
    }

    /**
     * Wrapper over ScheduledThreadPoolExecutor.
     */
    public static ScheduledExecutorService newDaemonSingleThreadScheduledExecutor(String threadName) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);
        // By default, a cancelled task is not automatically removed from the work queue until its delay
        // elapses. We have to enable it manually.
        executor.setRemoveOnCancelPolicy(true);
        return executor;
    }
}
