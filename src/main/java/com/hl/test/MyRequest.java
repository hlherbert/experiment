package com.hl.test;

import com.hl.Request;

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
