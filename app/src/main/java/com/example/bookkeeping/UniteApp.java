package com.example.bookkeeping;

import android.app.Application;

import com.example.bookkeeping.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UniteApp extends Application {

    private static ExecutorService executorService;

    // 获得ExecutorService的实例
    public static ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库对象
        // new Thread(()->{DBManager.initDB(getApplicationContext());}).start();

        // 使用Executor框架初始化数据库对象
        // 1. 创建Runnable接口并重写run()方法
        Runnable runnable = ()->DBManager.initDB(getApplicationContext());
        // 2. 创建ExecutorService实例，执行任务
        executorService = Executors.newFixedThreadPool(5);
        executorService.execute(runnable);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
            executorService.shutdownNow();
        }
    }
}
