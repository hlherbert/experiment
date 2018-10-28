package com.hl;

import java.io.IOException;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RequestProcessor implements Runnable {

    protected Queue<Request> requestQueue = null;

    /**
     * 是否并行处理
     * 默认是顺序处理queue,先进先处理
     */
    protected boolean parallel = false;


    public void processRequest(Request request) {
        System.out.println("process request:"+request);
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public void start() throws IOException {
        if (requestQueue == null) {
            throw new IOException("reqeust queue has not been initialized.");
        }
        Stream<Request> stream = parallel?requestQueue.parallelStream():requestQueue.stream();
        stream.forEach(req->processRequest(req));
    }

    @Override
    public void run(){
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
