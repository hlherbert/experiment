package com.hl.javabase.jmx;

/**
 * UserMBean实现
 * 一个受JMX监控对象
 * 可以注册到MBeanServer上，然后通过客户端工具如jconsole,连接到MBeanServer,去查看、修改MBean的属性，或者调用它的方法。
 */
public class User implements UserInterface {
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