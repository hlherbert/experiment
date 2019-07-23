package com.hl.javabase.thread;

import java.util.Arrays;

public class ThreadLocalTask implements Runnable {

    // 线程独立的属性
    private ThreadLocal<Integer> count = new ThreadLocal<>();

    // 子线程可以继承该属性, 可用于传递 User ID, Transaction ID
    private InheritableThreadLocal<Integer> countInherited = new InheritableThreadLocal<>();

    private static final int MAX_THREAD = 10;
    private static volatile int threadCount = MAX_THREAD;

    @Override
    public void run() {
        if (threadCount <= 0) {
            return;
        }

        // 子线程不可继承
        for (ThreadLocal<Integer> t : Arrays.asList(count, countInherited)) {
            if (t.get() == null) {
                t.set(0);
            }
            int c = t.get() + 1;
            t.set(c);
        }

        System.out.println("thread:" + Thread.currentThread().getName()
                + " count:" + count.get()
                + " countInherited:" + countInherited.get());

        threadCount--;
        Thread childThread = new Thread(this);
        childThread.start();
    }

    public static void main(String[] args) {
        ThreadLocalTask task = new ThreadLocalTask();
        new Thread(task).start();
    }
}
