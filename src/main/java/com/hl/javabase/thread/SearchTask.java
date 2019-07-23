package com.hl.javabase.thread;

import java.util.concurrent.TimeUnit;

public class SearchTask implements Runnable {

    private final Integer[] numbers;
    private final int targetNumber;

    public SearchTask(int targetNumber, Integer[] numbers) {
        this.numbers = numbers;
        this.targetNumber = targetNumber;
    }

    @Override
    public void run() {
        search();
    }

    private void search() {
        for (int i=0;i<numbers.length;i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("I am interrupted.");
                return;
            }

            if (numbers[i] == targetNumber) {
                System.out.println("found number!");
                return;
            }
        }
    }
}
