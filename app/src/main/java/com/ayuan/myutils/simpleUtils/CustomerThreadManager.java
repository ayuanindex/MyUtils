package com.ayuan.myutils.simpleUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CustomerThreadManager {
    /**
     * corePoolSize: 该线程池中核心线程的数量。
     * maximumPoolSize：该线程池中最大线程数量。(区别于corePoolSize)
     * keepAliveTime：非核心线程空闲时要等待下一个任务到来的时间
     * unit:上面时间属性的单位
     * workQueue:任务队列，后面详述。
     * threadFactory:线程工厂，可用于设置线程名字等等，一般无须设置该参数。
     */
    /*public final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            15,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5, true),
            (Runnable r) -> {
                Thread thread = new Thread(r);
                thread.setName("thread");
                thread.start();
                return thread;
            },
            new ThreadPoolExecutor.AbortPolicy()
    );*/


    /**
     * 获取当前CPU的核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 设置线程池的核心数2-4之间，但是取决于CPU的核心数
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    /**
     * 创建线程池
     */
    /*public static final ExecutorService executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);*/

    /**
     * corePoolSize-->最大核心线程数
     * maximumPoolSize-->线程池能容纳的最大线程数量
     * keepAliveTime-->线程的存活时间
     * TimeUnit-->时间单位
     * BlockingQueue-->缓存队列
     * ThreadFactory-->线程工厂
     * RejectedExecutionHandler-->拒绝策略
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            10,
            15,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5, true),
            (Runnable r) -> {
                Thread thread = new Thread(r);
                thread.setName("thread");
                thread.start();
                return thread;
            },
            new ThreadPoolExecutor.AbortPolicy()
    );
}
