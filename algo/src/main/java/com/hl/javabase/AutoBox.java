package com.hl.javabase;

public class AutoBox {
    public static void main(String[] args) {
        int x = new Integer(5);
        ClassLoader c = Thread.currentThread().getContextClassLoader();

        x = 5;

    }
}
