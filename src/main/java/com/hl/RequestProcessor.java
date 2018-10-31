package com.hl;

import java.io.IOException;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RequestProcessor implements Runnable {

    protected BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    /**
     * 是否并行处理
     * 默认是顺序处理queue,先进先处理
     */
    protected boolean parallel = false;

    /**
     * volatile是为了各个线程共享可见性。 running标记是否应该执行开关。
     */
    protected volatile boolean running = false;


    public void processRequest(Request request) {
        System.out.println("process request:"+request);
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public void startProcess()  {
//        try {
//            Stream.generate(()->{
//                return requestQueue.take();
//            }).forEach(req->Optional.ofNullable(req).ifPresent(this::processRequest));
//        } catch (InterruptedException e) {
//            System.err.println("thread interrupt. stop process");
//        }

        running = true;
        try {
            while (running) {

                Request req = requestQueue.take();
                processRequest(req);



//                requestQueue
//                        //.parallelStream()
//                        .stream()
//                        .forEach(req->{
//                            requestQueue.remove(req);
//                            processRequest(req);
//                        });

                System.err.println("i am waiting");
            }
        } catch (InterruptedException e) {
            System.out.println("take request is interrupted. stop running.");
            running = false;
        }
        System.err.println("processor is stopped.");
        running = false;
    }

    public void pushRequest(Request request) {
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("put request is interrupted.");
        }
    }

    public void stopProcess() {
        running = false;
    }

    @Override
    public void run(){
        startProcess();
    }
}
