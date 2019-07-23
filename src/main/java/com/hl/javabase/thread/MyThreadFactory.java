package com.hl.javabase.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {
    private int count = 0;
    private List<Thread> threads = new ArrayList<>();

    @Override
    public Thread newThread(Runnable r) {
        count++;
        return new Thread(r);
    }

    public void statistic() {
        System.out.printf("thread factory stat: thread count=%s \n", count);
    }
}
