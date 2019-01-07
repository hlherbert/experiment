package com.hl.javabase.jmx;

/**
 * UserMBean接口
 */
public interface UserMBean {
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