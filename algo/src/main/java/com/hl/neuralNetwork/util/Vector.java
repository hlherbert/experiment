package com.hl.neuralNetwork.util;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * 向量
 */
public class Vector implements Cloneable {
    public double[] x;

    public Vector(int n) {
        x = new double[n];
    }

    public Vector(Vector b) {
        x = Arrays.copyOf(b.x, b.x.length);
    }

    public Vector clone() {
        return new Vector(this);
    }

    /**
     * 归一化
     * 将向量长度统一为单位长度
     */
    public Vector normalize() {
        double len = Math.sqrt(Arrays.stream(x).map(z -> z * z).sum());
        for (int i = 0; i < x.length; i++) {
            x[i] /= len;
        }
        return this;
    }

    /**
     * 初始化为随机值
     */
    public Vector random() {
        for (int i = 0; i < x.length; i++) {
            x[i] = Math.random();
        }
        return this;
    }

    /**
     * 计算点积
     *
     * @return 点积
     */
    public double dot(Vector v) {
        int n = v.x.length;
        int m = this.x.length;
        if (m != n) {
            throw new InvalidParameterException("cannot dot.");
        }
        double val = 0;
        for (int i = 0; i < n; i++) {
            val += this.x[i] * v.x[i];
        }
        return val;
    }

    public Vector add(Vector v) {
        int n = v.x.length;
        int m = this.x.length;
        if (m != n) {
            throw new InvalidParameterException("cannot sub.");
        }
        Vector v1 = this.clone();
        for (int i = 0; i < n; i++) {
            v1.x[i] += v.x[i];
        }
        return v1;
    }

    public Vector sub(Vector v) {
        int n = v.x.length;
        int m = this.x.length;
        if (m != n) {
            throw new InvalidParameterException("cannot sub.");
        }
        Vector v1 = this.clone();
        for (int i = 0; i < n; i++) {
            v1.x[i] -= v.x[i];
        }
        return v1;
    }

    /**
     * 乘以标量m
     *
     * @param m 放大倍数
     * @return
     */
    public Vector mul(double m) {
        int n = this.x.length;
        Vector v1 = this.clone();
        for (int i = 0; i < n; i++) {
            v1.x[i] *= m;
        }
        return v1;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        int n = this.x.length;
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%.2f", this.x[i]) + ",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
