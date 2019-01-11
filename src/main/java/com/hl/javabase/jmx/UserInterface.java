package com.hl.javabase.jmx;

import javax.management.MXBean;

/**
 * MBean是一个受监控对象，
 * Jmx规范要求定义MBean需要先定义MBean接口，提供get,set等方法
 * 为使Jmx协议可以识别，接口必须命名为XxxMBean, 或者加注解@MXBean
 */
@MXBean
public interface UserInterface {
    /**
     * 获取名称
     * @return 名称
     */
    String getName();

    /**
     * 设置名称
     * @param name 名称
     */
    void setName(String name);

    /**
     * 加法运算
     * @param a 算子1
     * @param b 算子2
     * @return a+b
     */
    int add(int a, int b);
}