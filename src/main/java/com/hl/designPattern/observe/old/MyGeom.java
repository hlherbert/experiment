package com.hl.designPattern.observe.old;

import java.util.Observable;

public class MyGeom extends Observable {
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
        this.setChanged();
        this.notifyObservers();
    }

    private int len = 10;



}
