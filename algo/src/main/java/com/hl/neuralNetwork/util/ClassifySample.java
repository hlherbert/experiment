package com.hl.neuralNetwork.util;

/**
 * 分类样本
 */
public class ClassifySample {
    public Vector v; //样本向量
    public int klass; //所属分类

    public ClassifySample(Vector v, int klass) {
        this.v = v;
        this.klass = klass;
    }

    public ClassifySample(Vector v) {
        this.v = v;
        this.klass = 0;
    }
}
