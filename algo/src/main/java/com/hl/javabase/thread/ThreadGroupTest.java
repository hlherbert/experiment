package com.hl.javabase.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 线程组测试
 */
public class ThreadGroupTest {


    public static void main(String[] args) throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("SearchTaskGroup");
        List<Integer> numbers = Stream.generate(() -> Math.random() * 100)
                .map(Double::intValue)
                .limit(100000).collect(Collectors.toList());


        int targetNumber = 20;

        for (int i = 0; i < 3; i++) {
            List<Integer> subNumbers = numbers.subList(0, numbers.size() / 10 * (i + 1));
            SearchTask task = new SearchTask(targetNumber, subNumbers.toArray(new Integer[0]));
            Thread t = new Thread(threadGroup, task);
            t.start();
        }

        //输出线程信息
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for (int i = 0; i < threads.length; i++) {
            System.out.println("Thread " + threads[i].getName() + " State:" + threads[i].getState());
        }

        // 等待5秒
        TimeUnit.SECONDS.sleep(5);

        // 不管有无结果，都退出。
        System.out.println("Interrupt tasks.");
        threadGroup.interrupt();
    }
}
