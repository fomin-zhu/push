package com.fomin.push.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Fomin on 2018/10/23.
 */
public class ThreadUtil {
    private static class SingletonInstance {
        private static final ThreadUtil INSTANCE = new ThreadUtil();
    }

    public static ThreadUtil getInstance() {
        return ThreadUtil.SingletonInstance.INSTANCE;
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private ThreadUtil() {

    }

    /**
     * 在线程中执行
     *
     * @param runnable 要执行的runnable
     */
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

}
