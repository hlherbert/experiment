package com.hl.designPattern.observe;

/**
 * 被观察对象
 */
public interface MyObservable {
    /**
     * 增加观察者
     *
     * @param observer 观察者
     */
    void addObserver(MyObserver observer);

    /**
     * 自身有所变化，通知观察者更新
     */
    void notifyObjservers();
}
