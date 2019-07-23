package com.hl.designPattern.observe;

public interface MyObserver {
    /**
     * 观察对象改变后，对应的处理
     */
    void onObservableChanged(MyObservable observable);
}
