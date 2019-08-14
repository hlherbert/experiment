package com.hl.algo.divideCity;

import java.util.Set;
import java.util.TreeSet;

// 点
public class Point {
    // 序号
    int id;
    int degree;
    Set<Integer> neighbourPoints = new TreeSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Point() {}

    public Point(int id, int degree) {
        this.id = id;
        this.degree = degree;
    }

    @Override
    public String toString() {
        return ""+id;
    }
}
