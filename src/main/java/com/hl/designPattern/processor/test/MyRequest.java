package com.hl.designPattern.processor.test;

import com.hl.designPattern.processor.Request;

public class MyRequest implements Request {
    private int id;
    public MyRequest(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("request[%d]",id);
    }
}
