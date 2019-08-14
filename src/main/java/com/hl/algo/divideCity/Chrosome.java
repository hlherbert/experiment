package com.hl.algo.divideCity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 染色体
 */
public class Chrosome {
    // 随机发生器
    private static Random rand = new Random();

    // A点集
    private List<Point> A = new ArrayList<>();

    // B点集
    private List<Point> B = new ArrayList<>();

    // 边
    private Edge[] edges;

    // 适应度
    private int fitness = -1;

    // 克隆
    public Chrosome clone() {
        Chrosome c = new Chrosome();
        c.A.addAll(A);
        c.B.addAll(B);
        c.edges = edges;
        return c;
    }

    public List<Point> getA() {
        return A;
    }

    public void setA(List<Point> a) {
        A = a;
    }

    public List<Point> getB() {
        return B;
    }

    public void setB(List<Point> b) {
        B = b;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    // 求适应度，值越大，适应度越高
    public int fit() {
        if (fitness >= 0) {
            return fitness;
        }

        // 对本问题，适应度= A中连通点+B中连通点数
        int aSize = floodTreeSize(A, edges);
        int bSize = floodTreeSize(B, edges);
        int fit = aSize + bSize;
        fitness = fit;
        return fit;
    }

    // 本染色体是否是可行解
    public boolean isSolved(int fit) {
        // 当适应度=size(A)+size(B)时，表示找到可行解
        return fit == A.size() + B.size();
    }

    // 用flood法求图（Points, Edges）的最大生成树的点数
    private static int floodTreeSize(List<Point> points, Edge[] edges) {
        // 候选点集：S，最先是所有点
        // 已连通集：P
        // i: 连通集的索引
        // 0.当S为空时，跳到步骤5.
        // 1.从S中任选一个点p, 加入P
        // 2.判断S中是否有其他点p2和P相连，如果有，则将p2加入到P。
        // 3.重复步骤2，直到没有可选的点能再加入P。
        // 4.i++. 将P归为P[i]，清空P，跳到步骤1
        // 5.找出所有连通集P[i]中size最大值，做的最大生成树的点数。

        List<Point> S = new LinkedList<>();
        S.addAll(points);

        List<List<Point>> listP = new ArrayList<>();

        while (!S.isEmpty()) {
            List<Point> P = new ArrayList<>();
            Point p = S.remove(rand.nextInt(S.size()));
            P.add(p);

            Point p2;
            for (; ; ) {
                p2 = findConnectPoint(S, P, edges);
                if (p2 == null) {
                    break;
                }
                S.remove(p2);
                P.add(p2);
            }
            listP.add(P);
        }

        int maxSize = listP.stream().mapToInt(pts -> pts.size()).max().getAsInt();
        return maxSize;
    }

    // 从集合S中找到一个点和集合P相连接
    // 如果没找到返回null
    private static Point findConnectPoint(List<Point> S, List<Point> P, Edge[] edges) {
        for (Point p : S) {
            if (isConnect(p, P)) {
                return p;
            }
        }
        return null;
    }

    // 判断点p是否和点集合P连通
    private static boolean isConnect(Point p, List<Point> P) {
        // 寻找p的所有相邻点，是否有P集合中的点
        for (Point pnt : P) {
            if (p.neighbourPoints.contains(pnt.getId())) {
                return true;
            }
        }
        return false;
    }

    // 交叉
    // 从A,B中各选一个点互相交换
    public void crossOver() {
        int i = rand.nextInt(A.size());
        int j = rand.nextInt(B.size());
        Point pntA = A.get(i);
        A.set(i, B.get(j));
        B.set(j, pntA);
    }
}
