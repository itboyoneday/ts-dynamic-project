package com.tsingsoft.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yangjiawei
 * @date 2020-08-24 10:24
 **/
@Slf4j
public class ThreadPoolUtils {

    public static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() + 2;

    public static final int POOL_SIZE_MAX = (Runtime.getRuntime().availableProcessors() + 2) * 2;

    /**
     * 核心线程8,最大16
     * 存活时间1s
     * 非阻塞队列
     * @return 线程池
     * @author yangjiawei
     * @date 2020/8/24 10:28
     */
    public static ThreadPoolExecutor getDefaultThreadPoolExecutor(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("thread-%d").build();
        return new ThreadPoolExecutor(8, 16, 1000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ThreadPoolExecutor getFitThreadPoolExecutor(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("thread-%d").build();
        return new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE_MAX, 1L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 开启线程池
     * @return 线程池
     * @author yangjiawei
     * @date 2021/3/27 17:26
     */
    public static ExecutorService startPool(String factoryName){
        cn.hutool.core.thread.ThreadFactoryBuilder factoryBuilder = cn.hutool.core.thread.ThreadFactoryBuilder.create();
        factoryBuilder.setNamePrefix(factoryName);
        return new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE_MAX, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), factoryBuilder.build(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    // ----------------- lijian-----------------

    /**
     * 线程池关闭前等待时长
     */
    private static final int AWAIT_TIME = 5_000;

    /**
     * 初始化线程池
     *
     * @return 线程池
     */
    public static ExecutorService initPool() {
        return initPool("pool-%d");
    }

    /**
     * 初始化线程池 - 重命名线程
     *
     * @param nameFormat 线程命名格式，例如：CustHis96Lc-pool-%d
     * @return 线程池
     */
    public static ExecutorService initPool(String nameFormat) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
        return new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 关闭线程池
     * shutdown() 只起到通知的作用，需要进一步保证线程池关闭
     *
     * @param pool 线程池
     */
    public static void shutdownPool(ExecutorService pool) {
        try {
            pool.shutdown();
            // 所有的任务都结束的时候，返回true
            if (pool.awaitTermination(AWAIT_TIME, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
            log.info("================================== 线程池关闭成功!!! ==================================");
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            log.error("awaitTermination: " + e.getMessage());
            pool.shutdownNow();
        }

    }
}
