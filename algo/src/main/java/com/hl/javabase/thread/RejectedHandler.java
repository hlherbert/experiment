package com.hl.javabase.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 被拒绝任务处理器
 */
public class RejectedHandler implements RejectedExecutionHandler {
    private Logger logger = LoggerFactory.getLogger(RejectedHandler.class);

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory factory = new MyThreadFactory();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(factory);
        executor.setRejectedExecutionHandler(new RejectedHandler());
        executor.submit(new Task("eat"));
        executor.shutdown();

        // execute和submit区别: execute无返回值; submit返回Future
        executor.execute(new Task("swim"));
        executor.submit(new Task("drink"));
        executor.awaitTermination(1, TimeUnit.DAYS);

        ((MyThreadFactory) factory).statistic();

        System.out.println("end of main.");
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.info("Rejected Task: task={}, excutor={}, terminating={},terminated={}",
                r, executor, executor.isTerminating(), executor.isTerminated());
    }
}
