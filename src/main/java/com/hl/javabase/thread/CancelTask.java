package com.hl.javabase.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 演示取消一个任务
 */
public class CancelTask implements Callable<String> {

    @Override
    public String call() {
        for(;;){
            System.out.println("task running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("task interrupted.");
                return "Interrupted.";
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        CancelTask task = new CancelTask();
        Future<String> future = executor.submit(task);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Cancel the task.");
        future.cancel(true);
        executor.shutdown();

        try {
            String result = future.get();
            System.out.println("result="+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("main end.");
    }
}
