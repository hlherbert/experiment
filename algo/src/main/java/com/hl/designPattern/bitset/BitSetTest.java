package com.hl.designPattern.bitset;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.function.Function;

/**
 * BitSet例子
 * 将整数打印为二进制
 */
public class BitSetTest {

    private static String toBinary0(Integer n) {
        // to binary string
        return Integer.toBinaryString(n);
    }

    private static String toBinary1(Integer n) {
        //将N打印为二进制
        if (n == 0) {
            return "0";
        }
        StringBuffer b = new StringBuffer();
        boolean minus = false;
        if (n < 0) {
            minus = true;
            n = -n;
        }

        //  bitcount
        while (n != 0) {
            int x = n & 1;
            b.append(x);
            n = n >> 1;
        }

        b.reverse();
        return (minus ? "-" : "") + b.toString();
    }

    private static String toBinary2(Integer n) {
        if (n == 0) {
            return "0";
        }

        BitSet bitSet = new BitSet(64);
        StringBuffer s = new StringBuffer();
        if (n < 0) {
            n = -n;
            s.append("-");
        }
        int i = 0;
        while (n != 0) {
            int b = n & 1;
            if (b == 1) {
                bitSet.set(i);
            }
            n >>= 1;
            i++;
        }

        for (int j = 0; j < i; j++) {
            s.append(bitSet.get(i - j - 1) ? 1 : 0);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int n = -123;
        Function<Integer, String> f0 = BitSetTest::toBinary0;
        Function<Integer, String> f1 = BitSetTest::toBinary1;
        Function<Integer, String> f2 = BitSetTest::toBinary2;
        List<Function<Integer, String>> func = Arrays.asList(f0, f1, f2);

        for (int i = 0; i < func.size(); i++) {
            String b = func.get(i).apply(n);
            System.out.println(b);
        }
    }
}
