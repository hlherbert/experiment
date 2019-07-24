package com.hl.algo.sameval;

/**
 * n个数x[i]形成数列X，x[i]范围在0...n-1内，找出任意数x[j],在数列中有重复出现。
 * 算法空间复杂度需要O(1),时间复杂度O(n)
 */
public class Sameval {
    /**
     寻找x[]中的重复项
     @return 重复项x[j],如果未找到，返回-1
     */
    static int same(int[] x, int n) {
        long s = 0;

        // O(n)
        for (int i=0;i<x.length;i++) {
            s += Math.pow(n,x[i]);
        }

        // s = sum( c[i]*(n^i) )
        System.out.println("s="+s);
        final long ss = s;
        // O(n)
        for (int i=n-1;i>=0;i--) {
            long w = (long)Math.pow(n,i); //n^i
            long c = (ss % (w*n) )/ w;
            if (c > 1) {
                //System.out.printf("s=%d, w=%d, c=%d\n",s,w,c);
                return i;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        int n = 10;

        int[] x1 = {0,8,8,8,8,8,8,8,1, 9}; // 8
        int[] x2 = {0,1,2,3,4,5,2,7,8, 9}; // 2
        int[] x3 = {0,1,2,3,4,5,6,7,8, 0}; // 0
        int[] x4 = {0,1,2,3,4,5,6,7,8, 9}; // -1
        int[] x5 = {0,1,2,3,4,5,6,7,9, 9}; // 9
        System.out.println(same(x1, x1.length));
        System.out.println(same(x2, x2.length));
        System.out.println(same(x3, x3.length));
        System.out.println(same(x4, x4.length));
        System.out.println(same(x5, x5.length));
    }
}
