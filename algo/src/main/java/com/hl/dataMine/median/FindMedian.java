package com.hl.dataMine.median;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 快速求大数据量的中位数
 * 使用最大堆，最小堆
 * <p>
 * 将数组中的数据逐个放入两个子集，maxHeap, minHeap中
 * maxHeap是最大堆，minHeap是最小堆
 * 需要保持maxH和minH的平衡, 且maxH的顶部始终小于minH的顶部
 */
public class FindMedian {


    public static double findMedian(double[] data) {
        PriorityQueue<Double> minHeap = new PriorityQueue(data.length / 2 + 1);
        PriorityQueue<Double> maxHeap = new PriorityQueue(data.length / 2 + 1, Comparator.naturalOrder().reversed());

        for (double x : data) {
            if (maxHeap.isEmpty()) {
                maxHeap.add(x);
            } else if (maxHeap.size() > minHeap.size()) {
                // .   |
                if (x > maxHeap.element()) {
                    minHeap.add(x);
                } else {
                    double y = maxHeap.remove();
                    minHeap.add(y);
                    maxHeap.add(x);
                }
            } else if (maxHeap.size() == minHeap.size()) {
                // . | .
                if (x > maxHeap.element()) {
                    minHeap.add(x);
                } else {
                    maxHeap.add(x);
                }
            } else if (maxHeap.size() < minHeap.size()) {
                // . | ..
                if (x > minHeap.element()) {
                    double y = minHeap.remove();
                    minHeap.add(x);
                    maxHeap.add(y);
                } else {
                    maxHeap.add(x);
                }
            }
        }

        if (minHeap.size() == maxHeap.size()) {
            return (minHeap.element() + maxHeap.element()) / 2.0;
        } else if (minHeap.size() > maxHeap.size()) {
            return minHeap.element();
        } else {
            return maxHeap.element();
        }
    }

    public static void main(String[] args) {
        double[] data = {5, 3, 3, 2, 1, 7, 9}; // 1,2,3,3,5,7,9
        data = new double[]{5, 10, 7, 6, 8};
        int i = 0;
        Stream<Double> stream = Stream.generate(Math::random);
        List<Double> zz = stream.limit(1000000).collect(Collectors.toList());
        data = new double[zz.size()];

        for (double z : zz) {
            data[i++] = z;
        }
        // 5,
        // 5, | 10
        // 5, | 7, 10
        // 5,6 | 7, 10                  5,7 | 6,10
        // 5,6 | 7, 8, 10               5,7 | 6,8,10
        // out:   7  -- right                    6 -- wrong
        double v = 0;
        long t0 = System.currentTimeMillis();
        for (i = 0; i < 10; i++) {
            v = findMedian(data);
        }
        System.out.println(v);
        long t1 = System.currentTimeMillis();
        for (i = 0; i < 10; i++) {
            v = StreamMedian.median(data);
        }
        System.out.println(v);
        long t2 = System.currentTimeMillis();
        for (i = 0; i < 10; i++) {
            v = FindMedianSimple.findMedian(data);
        }
        System.out.println(v);
        long t3 = System.currentTimeMillis();

        System.out.println("findMedian: " + (t1 - t0));
        System.out.println("StreamMedian: " + (t2 - t1));
        System.out.println("findMedianSimple: " + (t3 - t2));

        Double p = 4.2;
        if (p instanceof Comparable) {
            System.out.println("double is comparable");
        }
    }
}
