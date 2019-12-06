package com.hl.algo.random;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomTest {
    // select m numbers from 0~n
    public static int[] randomSelect(int n, int m) {
        Random random = new Random();
        int[] num = IntStream.range(0, n).toArray();
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
        //    System.out.print("num:" + Arrays.toString(num) + "\t");
            int index = random.nextInt(n - i);
            result[i] = num[index];
            num[index] = num[n - i - 1];
        //    System.out.println("result:" + Arrays.toString(result));
        }
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("==========");
            int[] sel = randomSelect(3, 2);
            System.out.println(Arrays.toString(sel));
        }
    }
}
