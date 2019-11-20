package com.hl.dataMine.median;

//题目：如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么
//中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，
//那么中位数就是所有数值排序之后中间两个数的平均值。

import java.util.Comparator;
import java.util.PriorityQueue;

public class StreamMedian {

    PriorityQueue<Double> minHeap = new PriorityQueue(); //小顶堆，默认容量为11
    PriorityQueue<Double> maxHeap = new PriorityQueue(11, Comparator.naturalOrder().reversed());

    public static double median(double[] data) {
        StreamMedian algo = new StreamMedian();
        for (double x : data) {
            algo.Insert(x);
        }
        return algo.GetMedian();
    }

    public void Insert(Double num) {
        if (((minHeap.size() + maxHeap.size()) & 1) == 0) {//偶数时,下个数字加入小顶堆
            if (!maxHeap.isEmpty() && maxHeap.peek() > num) {
                maxHeap.offer(num);
                num = maxHeap.poll();
            }
            minHeap.offer(num);
        } else {//奇数时，下一个数字放入大顶堆
            if (!minHeap.isEmpty() && minHeap.peek() < num) {
                minHeap.offer(num);
                num = minHeap.poll();
            }
            maxHeap.offer(num);
        }
    }

    public Double GetMedian() {
        if ((minHeap.size() + maxHeap.size()) == 0)
            throw new RuntimeException();
        double median;
        if ((minHeap.size() + maxHeap.size() & 1) == 0) {
            median = (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            median = minHeap.peek();
        }
        return median;
    }
}