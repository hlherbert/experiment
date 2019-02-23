package com.hl.neuralNetwork;

import com.hl.neuralNetwork.util.Vector;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自组织神经网
 */
public class Som {

    private Vector[] weight;

    public static void main(String[] args){
        test();
    }
    public static void test() {
        Vector v = new Vector(2);
        v.x[0]=3;
        v.x[1]=4;
        v = v.normalize();
        System.out.println(Math.sqrt(v.x[0]*v.x[0]+v.x[1]*v.x[1]));

        Som som = new Som();
        int m = 2; //输入向量的维度
        int n = 2; //分类数
        List<Vector> samples = Stream.generate(() -> new Vector(m).clone().random()).limit(10).collect(Collectors.toList());
        som.train(samples,m,n);

        for (Vector sample: samples) {
            int klass = som.classify(sample);
            System.out.println(MessageFormat.format("x={0},class={1}",sample,klass));
        }
    }


    /**
     * 优胜领域的半径
     * 优胜领域随时间收缩
     * @param t 时间
     */
    private int neighbour(int t) {
        int nei = 6; //初始大小
        nei *= Math.exp(-t); // nei = nei * e^(-t) 自然对数衰减
        return nei;
    }

    /**
     * 学习函数
     * 随时间下降，随领域距离下降
     * @param t 时间
     * @param nei 领域距离
     */
    private double learnFunc(int t, int nei) {
        // 激活函数：厨师帽函数, 在领域内都是1.
        double active = 1;

        // 退火函数，随时间衰减。
        return Math.exp(-t)*active;
    }
    /**
     * 训练
     * @param samples 训练样本
     * @param m 输入向量的维数
     * @param n 输出神经元个数
     */
    public void train(List<Vector> samples, int m, int n) {
        if (samples.size()<=0) {
            return;
        }

        // 初始化权值.
        weight = new Vector[n];// n个输出神经元对应 n个权值向量，每个向量有m维
        for (int i=0;i<n;i++) {
            weight[i] = new Vector(m).random().normalize();
        }

        // 输入归一化
        List<Vector> normSamples = samples.stream().map(v->v.clone().normalize()).collect(Collectors.toList());

        int t =0;
        // 对每个输入样本迭代
        for (int i=0;i<samples.size();i++) {
            double maxDot = 0;
            int winner = 0;

            //本次输入
            Vector x = normSamples.get(i);

            // 寻找获胜节点
            // 点积最大的
            for (int j=0;j<n;j++) {
                double dot = normSamples.get(i).dot(weight[j]);
                if (dot > maxDot) {
                    maxDot = dot;
                    winner = j;
                }
            }

            // 定义优胜领域, 随时间收缩
            int nei = neighbour(t);

            // 对优胜领域内所有节点调整权值。
            // w' = w + l(t,nei)*(x-w)
            for (int j=0;j<nei;j++) {
                int j0 = winner-j;
                int j1 = winner+j;
                if (j0>=0 && j0<n) {
                    weight[j0] = weight[j0].add(x.sub(weight[j0]).mul(learnFunc(t, j)));
                }
                if (j>0 && j1>=0 && j1<n) {
                    weight[j1] = weight[j1].add(x.sub(weight[j1]).mul(learnFunc(t, j)));
                }
            }

            t++;
        }
    }

    /**
     * 训练后，对输入样本进行分类
     * @param sample
     * @return
     */
    public int classify(Vector sample) {
        int n = weight.length;
        double maxDot = 0;
        int winner = 0;

        //本次输入归一化
        Vector normSample = sample.clone().normalize();

        // 寻找获胜节点
        // 点积最大的
        for (int j=0;j<n;j++) {
            double dot = normSample.dot(weight[j]);
            if (dot > maxDot) {
                maxDot = dot;
                winner = j;
            }
        }
        return winner;
    }
}
