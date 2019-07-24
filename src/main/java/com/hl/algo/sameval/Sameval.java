package com.hl.algo.sameval;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * n个数x[i]形成数列X，x[i]范围在0...n-1内，找出任意数x[j],在数列中有重复出现。
 * 算法空间复杂度需要O(1),时间复杂度O(n)
 */
public class Sameval {
    /**
     * 寻找x[]中的重复项
     * 方法1.
     *
     * @return 重复项x[j], 如果未找到，返回-1
     */
    static int same(Integer[] x, int n) {
        long s = 0;

        // O(n)
        for (int i = 0; i < x.length; i++) {
            s += Math.pow(n, x[i]);
        }

        // s = sum( c[i]*(n^i) )
        // System.out.println("s="+s);
        final long ss = s;
        // O(n)
        for (int i = n - 1; i >= 0; i--) {
            long w = (long) Math.pow(n, i); //n^i
            long c = (ss % (w * n)) / w;
            if (c > 1) {
                //System.out.printf("s=%d, w=%d, c=%d\n",s,w,c);
                return i;
            }
        }
        return -1;
    }


    /**
     * 方法2，用bitset存储x[i]出现的次数，遇到x[i]使能对应位k，如果发现k位已经使能了，则k重复
     * 但该方法会占用大量空间。
     *
     * @param x
     * @param n
     * @return
     */
    static int same2(Integer[] x, int n) {
        BitSet b = new BitSet(Integer.MAX_VALUE);
        for (int i = 0; i < x.length; i++) {
            int k = x[i];
            if (b.get(k)) {
                return k;
            } else {
                b.set(k);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int n = 10;

        Integer[] x1 = {0, 8, 8, 8, 8, 8, 8, 8, 1, 9}; // 8
        Integer[] x2 = {0, 1, 2, 3, 4, 5, 2, 7, 8, 9}; // 2
        Integer[] x3 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 0}; // 0
        Integer[] x4 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}; // -1
        Integer[] x5 = {0, 1, 2, 3, 4, 5, 6, 7, 9, 9}; // 9
        List<Integer[]> dataset = Arrays.asList(x1, x2, x3, x4, x5);

        final Function<Integer[], Integer> f1 = ((x) -> same(x, x.length));
        final Function<Integer[], Integer> f2 = ((x) -> same2(x, x.length));
        final List<Function<Integer[], Integer>> fList = Arrays.asList(f1, f2);
        for (Function<Integer[], Integer> f : fList) {
            System.out.println("func=" + f.toString());
            List<Integer> resutl1 = dataset.stream().map(f).collect(Collectors.toList());
            System.out.println(resutl1);
        }
    }
}
