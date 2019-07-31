package com.hl.algo.montecaral;

import java.util.Random;
import java.util.stream.Stream;

/**
 * 蒙特卡洛法求pi
 */
public class Pi {

    static final long N = 10000000;
    /**
     * 蒙特卡洛法求pi
     * 半径为1的圆O，外切正方形,边长为2
     * 从中线划分为4个相等部分。每一部分为一个半径为1的扇形OAC（1/4圆）和一个边长为1的正方形ABCD
     * 圆面积 Area(O) = PI * 1 = 4*Area(OAC)
     * 正方形面积 Area(ABCD) = 1*1 = 1
     * PI = 4* Area(OAC) / Area(ABCD)
     *
     * 向正方形ABCD内投入小球，重复多次，落入OAC的次数为M，落入正方型的次数为N
     * 当N -> infinity ， Area(OAC) / Area(ABCD) = M/N
     * pi = 4 * M / N
     *
     * 流程：
     * m = 0
     * for (i=0;i<N;i++):
     *   生成随机坐标(X,Y), X,Y in [0,1)
     *   if (x^2+y^2 < 1):
     *     m := m+1
     * pi = 4* m/N
     *
     * @return pi
     */
    public static double piMonteCarlo() {
        Random rnd = new Random();
        long m = Stream.generate(()->Math.pow(rnd.nextDouble(),2)+Math.pow(rnd.nextDouble(),2))
                .limit(N)
                .filter(z->z<1)
                .count();
        return 4.0*m/N;
    }

    /**
     * 多次蒙特卡洛算法求平均，逐渐逼近PI
     * @return PI的逼近值
     */
    public static double pi() {
        final long M = 100;
        double x = piMonteCarlo();
        for (int i=1;i<M;i++) {
            x = (x*i+piMonteCarlo())/(i+1);
            System.out.println(x);
        }
        return x;
    }

    public static void main(String[] args) {
        System.out.println("Pi = "+pi());
    }
}
