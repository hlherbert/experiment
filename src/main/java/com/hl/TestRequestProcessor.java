package com.hl;

public class TestRequestProcessor  {
    public static void main(String[] args) {
        RequestProcessor processor = new RequestProcessor();
        Thread processThread = new Thread(processor);
        processThread.start();
    }
}
