package com.hl.algo.divideCity;

public class Edge {
    // 连接边的两个点的序号
    int a;
    int b;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) obj;
        return (e.a == a && e.b == b) || (e.b == a && e.a == b);
    }
}
