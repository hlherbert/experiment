package com.hl.algo.divideCity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 图
public class Graph {
    // 点
    private List<Point> points = new ArrayList<>();


    public Graph() {}

    public Graph(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }


    public Graph clone() {
        Graph g = new Graph();
        g.points.addAll(points);
        return g;
    }

    // 对本图中每个点， 去掉不在本图中的相邻点
    public void cutNeighbourPoints() {
        List<Point> B = points;
        Set<Integer> pointIndexsB = B.stream().map(point->point.id).collect(Collectors.toSet());
        for (Point p:B) {
            Iterator<Integer> it = p.neighbourPoints.iterator();
            while (it.hasNext()) {
                if (!pointIndexsB.contains(it.next())) {
                    it.remove();
                }
            }
        }
    }
}
