package com.ken24k.android.mvpdemo.common.initialize;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangming on 2020-05-28
 */

public class ThreadManager {

    private static ThreadPollProxy mThreadPollProxy;

    /**
     * 单列对象
     */
    private static ThreadPollProxy getThreadPollProxy() {
        if (null == mThreadPollProxy) {
            synchronized (ThreadPollProxy.class) {
                if (mThreadPollProxy == null) {
                    mThreadPollProxy = new ThreadPollProxy(0, 10, 1000);
                }
            }
        }
        return mThreadPollProxy;
    }

    public static void executeProxy(Runnable runnable) {
        getThreadPollProxy().execute(runnable);
    }

    /**
     * 通过ThreadPoolExecutor的代理类来对线程池的管理
     */
    private static class ThreadPollProxy {

        private ThreadPoolExecutor poolExecutor;// 线程池执行者，java内部通过该api实现对线程池管理
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        private ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /**
         * 对外提供一个执行任务的方法
         */
        private void execute(Runnable runnable) {
            if (poolExecutor == null || poolExecutor.isShutdown()) {
                poolExecutor = new ThreadPoolExecutor(
                        // 核心线程数量
                        corePoolSize,
                        // 最大线程数量
                        maximumPoolSize,
                        // 当线程空闲时，保持活跃的时间
                        keepAliveTime,
                        // 时间单元，毫秒级
                        TimeUnit.MILLISECONDS,
                        // 线程任务队列
                        new LinkedBlockingQueue<Runnable>(),
                        // 创建线程的工厂
                        Executors.defaultThreadFactory());
            }
            poolExecutor.execute(runnable);
        }
    }
}
