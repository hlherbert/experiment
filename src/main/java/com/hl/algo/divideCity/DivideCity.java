package com.hl.algo.divideCity;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * IOI 题目:
 * n个城市，1~n
 * m个双向连接 1~m, 每个连接，连接两个城市a,b
 * 可行解：
 * 将n个城市划分为A,B,C三个子集（规模分别为a,b,c），满足：至少两个子集是连通的。
 * <p>
 * 输入：n,m,
 * m的连接，
 * A,B,C的规模a,b,c
 * <p>
 * 输出：
 * 任意一个可行解: 即集合A,B,C各自包含的具体城市标号
 * 如果无解，输出无解。
 * <p>
 * 规模：n<=2500, m<=5000
 * 时间限制：1分钟
 */
public class DivideCity {

    private static Random rand = new Random(1);

    /**
     * 算法分析
     * <p>
     * 任意两个集合C,D，集合大小C<D，如果一个城市组合(x1,x2,...xc)刚好放入C，且他们连通，则将其放入D中也必然连通。
     * 如果找不到这样的组合满足能放入C且连通，则也找不到这样的组合放入D连通。
     * 因此集合大小越小，就容易找到连通的子集。
     * <p>
     * a,b,c给定后，按照规模从小到大顺序排列，分别为a',b',c'，对应集合A',B',C'规模也是从小到大顺序。
     * 我们只要依次找到A',B'的组合，使A',B'连通，则找到了解。否则就是无解。
     * <p>
     * 无解的条件：
     * 如果有解，则至少有a',b'个点，以及它们的连接线个数 a'+b'-2
     * 如果m<a'+b'-2,则一定无解
     * <p>
     * 由于B'比A'难求，因此可以先计算B'，如果B'无解，则总体无解。
     * <p>
     * 先选a'个城市组合，再选b'个城市组合，总的可能的组合数为：C(n,a)*C(n-a,b)=n!/(a!(n-a)!)*(n-a)!/(b!(n-a-b)!)
     * 试算组合数很大，所以用穷举法不可行。
     */
    void analysis() {
        // 组合数最多的情况
        int n = 2500;
        int a = n / 2;
        int b = n - a;
        BigInteger nn = fac(n);
        BigInteger nComb1 = fac(n).divide(fac(a)).divide(fac(n - a));
        BigInteger nComb2 = fac(n - a).divide(fac(b)).divide(fac(n - a - b));
        BigInteger nComb = nComb1.multiply(nComb2);

        System.out.println("comb number:" + nComb);
        //comb number:5996747843879727010446034750704879594589642330081728571047014061721602054236827424066853583653214345317231901852809276951453488917246864436113036781633346653784807909853233470017653461203008934723274411227996848083467997291789654100090704530254271834955239446638598463463603725241943670645308798092914381274804810715111871026290607464929695407747361109295225211914838428789871075426430033781291071953038208600626781350439139868806691669992124884739760249982470690702558066909456447383538712647210360893379880018358591103233063966107174651963480056805263721863341173540121258450592572011293683093517188337776678516511322530224839233531808516500724345588736023386297879901427185034262239853347934813877454202900114017433903211294289102934197769359372256
    }


    /**
     * 求阶乘 x!
     *
     * @param x
     * @return x!
     */
    private static BigInteger fac(int x) {
        BigInteger p = BigInteger.ONE;
        for (int i = 1; i <= x; i++) {
            p = p.multiply(BigInteger.valueOf(i));
        }
        return p;
    }

    // 基因算法的最大迭代次数
    private final int MAX_GENERATION = 100000;

    // 集合A大小
    private int a;

    // 集合B大小
    private int b;

    // 集合C大小
    private int c;

    // 旧的a,b,c
    private int aOld, bOld, cOld;

    //节点总数
    private int n;

    // 线段总数
    private int m;

    // 点序号 0~n-1
    // 边
    private Edge[] edges = new Edge[m];

    // 各个点的度数
    //private int[] degrees = new int[n];

    // 需要考虑放入A,B中的点集合
    private List<Point> S = new LinkedList<>();

    //基因种群数量
    private static final int POPULATION_SIZE = 1000;

    /**
     * 算法：
     * 0.预处理, a,b,c排序，按照从小到大设置为a,b,c
     * 1.剪枝, 去掉所有degree(x) = 0的点x。  degree(x) = x的边数
     * 2.剩下的点S中，判断 if 总点数count(S) < a+b, 则无解
     * 3.对S的点，按照degree从大到小排序，选取前a+b个点
     * 4.用进化算法：
     * （1）初始化S中a个点放入A,b个点放入B，AB组成染色体C，求染色体的适应度fit。
     * （2）若代数g> 最大代数G, 则未找到解，退出.
     * （3) 判断A，B是否全连通，如果满足，则返回A,B；否则下一步.
     * （4）交换A,B中一对点，形成C'=A'B',求染色体适应度fit'
     * （5）如果fit'<fit, 则重复（4）;否则用新的染色体作为下一代：C=C'，代数g := g+1， 重复（2）
     */
    void solve() {
        input();
        prepare();
        cut();
        geneAlgo();
    }

    // 输入
    private void input() {
        System.out.println("input ...");

        n = 2500;
        m = 5000;

        a = 5;
        b = 5;
        c = n - a - b;

        aOld = a;
        bOld = b;
        cOld = c;

        edges = new Edge[m];

        // 各个点的度数
        // degrees = new int[n];

        // 随机生成边
        Set<Edge> edgeSet = new HashSet<Edge>();
        for (int i = 0; i < m; i++) {
            Edge edge = new Edge();
            edge.a = rand.nextInt(n);
            do {
                edge.b = rand.nextInt(n);
            } while (edge.a == edge.b || edgeSet.contains(edge));
            edges[i] = edge;
            edgeSet.add(edge);
        }
    }

    // 预处理
    private void prepare() {
        System.out.println("prepare ...");
        // a,b,c从新排序
        List<Integer> x = Arrays.asList(a, b, c);
        x.sort(Comparator.naturalOrder());
        a = x.get(0);
        b = x.get(1);
        c = x.get(2);

        // 计算并保存每个点的度数, 保存每个点的邻点
        for (int i = 0; i < n; i++) {
            Point p = new Point();
            p.setId(i);
            int degree = calcDegree(p);

            // 将度数不为0的点放入S
            if (degree > 0) {
                S.add(p);
            }
        }
    }

    // 计算度数
    // p: 点序号
    private int calcDegree(Point pnt) {
        // 从所有边中，寻找i出现的次数，只要出现1次，度数加1
        int d = 0;
        int p = pnt.id;
        for (int i = 0; i < m; i++) {
            if (edges[i].a == p) {
                d++;
                pnt.neighbourPoints.add(edges[i].b);
            } else if (edges[i].b == p) {
                d++;
                pnt.neighbourPoints.add(edges[i].a);
            }
        }
        pnt.setDegree(d);
        return d;
    }

    // 剪枝
    private void cut() {
        System.out.println("cut ...");
        if (S.size() < a + b) {
            System.out.println("No Answer! S.size < a+b");
            System.exit(0);
        }

        // 取前a+b个元素放入S
        //S = S.stream().sorted(Comparator.comparing(Point::getDegree).reversed()).limit(a + b).collect(Collectors.toList());

        System.out.println("S.size = " + S.size());
    }


    // 进化算法
    // 返回一个可行解
    // 如果没有可行解返回null
    private Chrosome geneAlgo() {
//    （1）初始化S中, 随机选择a个点放入A, b个点放入B，AB组成染色体C，求染色体的适应度fit。
//    （2）若代数g > 最大代数G, 则未找到解，退出.
//    （3)判断A，B是否全连通，如果满足，则返回A, B；否则下一步.
//    （4）交换A, B中一对点，形成C '=A' B ',求染色体适应度fit'
//    （5）如果fit '<fit, 则重复（4）;否则用新的染色体作为下一代：C=C'，代数g:=g + 1，重复（2）


        // 初始化种群
        List<Chrosome> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chrosome chrome = new Chrosome();
            geneInit(chrome);
            population.add(chrome);
        }

        for (int g = 0; g < MAX_GENERATION; g++) {
            if (g % 1000 == 0)
                System.out.println("gen:" + g);

            for (int i = 0; i < POPULATION_SIZE / 4; i++) {
                // 迭代
                Chrosome c1 = population.get(i * 2);
                int fit = c1.fit();
                if (c1.isSolved(fit)) {
                    System.out.println(MessageFormat.format("Solved!A:{0}  B:{1}", c1.getA(), c1.getB()));
                    proveConnection(c1.getA());
                    proveConnection(c1.getB());
                    return c1;
                }


                // 交叉
                Chrosome c2 = population.get(i * 2 + 1);
                int fit2 = c2.fit();
                if (c2.isSolved(fit2)) {
                    System.out.println(MessageFormat.format("Solved!A:{0}  B:{1}", c2.getA(), c2.getB()));
                    proveConnection(c2.getA());
                    proveConnection(c2.getB());
                    return c2;
                }

                Pair<Chrosome> childPair = Chrosome.crossOver(c1, c2);
                // 子代存在 i*3， i*4
                population.set(i * 2 + POPULATION_SIZE / 2, childPair.a);
                population.set(i * 2 + 1 + POPULATION_SIZE / 2, childPair.b);
            }
            // 对下一代按照适应度重新排序
            population.sort(Comparator.comparing(Chrosome::fit).reversed());
        }

        System.out.println("No Answer! Exceed max generation=" + MAX_GENERATION);
        return null;
    }

    // 证明点集是连通图
    private void proveConnection(List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            System.out.println(p1 + " neighbours:" + p1.neighbourPoints);
        }

    }

    // 进化算法初始化, 生成第一代种子
    private void geneInit(Chrosome c) {
        LinkedList<Point> S2 = new LinkedList<>();
        S2.addAll(S);
        List<Point> A = c.getA();
        List<Point> B = c.getB();
        A.clear();
        B.clear();

        // 前a个放入A
        for (int i = 0; i < a; i++) {
            int p = rand.nextInt(S2.size());
            A.add(S2.remove(p));
        }

        // b个放入B
        for (int i = 0; i < b; i++) {
            int p = rand.nextInt(S2.size());
            B.add(S2.remove(p));
        }
    }

    public static void main(String[] args) {
        DivideCity d = new DivideCity();
        d.analysis();
        //System.out.println(fac(0));
        d.solve();
    }
}
