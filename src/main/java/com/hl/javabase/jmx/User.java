package com.hl.javabase.jmx;

/**
 * UserMBean实现
 */
public class User implements UserMBean {
    /**
     * User名称
     */
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}