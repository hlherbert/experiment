package com.hl.javabase.thread;

public class Task implements Runnable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("task " + name + " is running.");
    }

    @Override
    public String toString() {
        return "task " + name;
    }
}
