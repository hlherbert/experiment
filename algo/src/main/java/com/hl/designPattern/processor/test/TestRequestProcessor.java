package com.hl.designPattern.processor.test;

import com.hl.designPattern.processor.RequestProcessor;

import java.util.Scanner;

public class TestRequestProcessor {
    public static void main(String[] args) throws InterruptedException {
        RequestProcessor processor = new RequestProcessor();
        Thread processThread = new Thread(processor);


//        new Thread( new Runnable() {
//            @Override
//            public void run() {
//                int n = 0;
//                for (int i = 0; i < 10; i++) {
//                    for (int j=0;j<10;j++) {
//                        MyRequest req = new MyRequest(n++);
//                        processor.pushRequest(req);
//                    }
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        processThread.start();

        Scanner console = new Scanner(System.in);
        int n = 0;
        while (true) {
            String line = console.nextLine();
            if (line == null) {
                continue;
            }
            if ("exit".equals(line)) {
                break;
            }
            System.err.println("push 10 numbers.");
            for (int j = 0; j < 10; j++) {
                MyRequest req = new MyRequest(n++);
                processor.pushRequest(req);
            }
        }
        System.err.println("exit.");
        //System.out.println("now will stop running.");
        //processor.stopProcess();
    }
}
