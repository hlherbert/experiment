package com.hl.dataMine.median;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 快速求大数据量的中位数 -简化版本
 * 该方法代码比较简单但是性能比FindMedian略低。因为当总数为偶数时，有可能要插入两次。
 * <p>
 * 使用最大堆，最小堆
 * <p>
 * 将数组中的数据逐个放入两个子集，maxHeap, minHeap中
 * maxHeap是最大堆，minHeap是最小堆
 * 需要保持maxH和minH的平衡, 且maxH的顶部始终小于minH的顶部
 */
public class FindMedianSimple {

    public static double findMedian(double[] data) {
        PriorityQueue<Double> minHeap = new PriorityQueue(data.length / 2 + 1);
        PriorityQueue<Double> maxHeap = new PriorityQueue(data.length / 2 + 1, Comparator.naturalOrder().reversed());

        //{5, 10, 7, 6, 8};
        // 5,
        // 5, | 10
        // 5,7  | 10
        // 5,6  | 7,10
        // 5,6,7 | 8,10
        for (double x : data) {
            if (((maxHeap.size() ^ minHeap.size()) & 0x1) == 0) {
                //偶数，始终往maxHeap添加数据，保持它比minHeap多一个元素
                if (maxHeap.isEmpty() || x < maxHeap.element()) {
                    maxHeap.add(x);
                } else {
                    minHeap.add(x);
                    double y = minHeap.remove();
                    maxHeap.add(y);
                }
            } else {
                //奇数, 始终往minHeap添加数据，保持两个heap大小一致
                if (minHeap.isEmpty() || x > minHeap.element()) {
                    minHeap.add(x);
                } else {
                    maxHeap.add(x);
                    double y = maxHeap.remove();
                    minHeap.add(y);
                }
            }
        }

        // 这种算法maxHeap.size 始终>= min.size
        if (minHeap.size() == maxHeap.size()) {
            return (minHeap.element() + maxHeap.element()) / 2.0;
        } else {
            return maxHeap.element();
        }
    }
}
