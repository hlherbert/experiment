package com.hl.designPattern.observe;

import java.util.ArrayList;
import java.util.List;

public class MyShape implements MyObservable {
    private List<MyObserver> observers = new ArrayList<>();
    private int length = 5;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (this.length == length) {
            return;
        }
        this.length = length;
        notifyObjservers();
    }

    @Override
    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObjservers() {
        for (MyObserver observer : observers) {
            observer.onObservableChanged(this);
        }
    }
}
