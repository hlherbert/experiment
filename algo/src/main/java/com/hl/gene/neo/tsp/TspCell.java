package com.hl.gene.neo.tsp;

import com.hl.gene.neo.core.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * 解决旅行商问题的个体
 */
public class TspCell implements Cell {

    private static final Logger logger = LoggerFactory.getLogger(TspCell.class);
    /**
     * 基因随机发生器
     */
    private static Random geneRandom = new Random();

    /**
     * 变异随机发生器
     */
    private static Random mutationRandom = new Random();

    /**
     * 基因交叉随机发生器
     */
    private static Random crossoverRandom = new Random();

    /**
     * 基因序列
     */
    private int[] genes;

    /**
     * 适应度
     */
    private double fit;

    /**
     * 适应度是否需要重算
     */
    private boolean fitDirty = true;

    /**
     * 对应的问题
     */
    private TspProblem problem;

    public TspCell(TspProblem problem) {
        this.problem = problem;
        genes = new int[problem.nCity];
    }

    @Override
    public void randomInit() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = geneRandom.nextInt(problem.nCity);
        }
        dirty();
    }

    @Override
    public double fitness() {
        if (!fitDirty) {
            return fit;
        }

        updateFitness();
        return fit;
    }

    @Override
    public Cell cloneCell() {
        TspCell cell = new TspCell(problem);
        cell.genes = genes.clone();
        cell.fit = fit;
        cell.fitDirty = fitDirty;
        return cell;
    }

    @Override
    public Cell birthBisex(Cell spouse) {
        // 有性繁殖
        if (!(spouse instanceof TspCell)) {
            logger.error("spouses must be same class.");
            return null;
        }
        TspCell parent1 = this;
        TspCell parent2 = (TspCell) spouse;

        if (parent1.genes.length != parent2.genes.length) {
            logger.error("gene length of two parents must be same.");
            return null;
        }

        // crossover 交叉选择
        TspCell child = (TspCell) parent1.cloneCell();
        for (int i = 0; i < child.genes.length; i++) {
            if (crossoverRandom.nextInt(2) == 0) {
                child.genes[i] = parent1.genes[i];
            } else {
                child.genes[i] = parent2.genes[i];
            }
        }
        child.dirty();
        return child;
    }

    @Override
    public void mutate() {
//        // 随机选择序列里的两个位置上的基因，交换
//        int pos1 = mutationRandom.nextInt(genes.length);
//        int pos2 = mutationRandom.nextInt(genes.length);
//        int g1 = genes[pos1];
//        genes[pos1] = genes[pos2];;
//        genes[pos2] = g1;

        // 随机选择序列里一个位置上的基因，将其值改为另一个随机值
        int pos1 = mutationRandom.nextInt(genes.length);
        genes[pos1] = geneRandom.nextInt(problem.nCity);

        dirty();
    }

    /**
     * 记录状态改变了，需重算
     */
    private void dirty() {
        fitDirty = true;
    }

    /**
     * 记录状态已经更新为最新
     */
    private void updated() {
        fitDirty = false;
    }

    /**
     * 更新适应度
     *
     * @return 新的适应度
     */
    private void updateFitness() {
        //重算适应度
        fit = computeFitness();

        // 恢复状态
        updated();
    }

    /**
     * 根据基因，计算适应度
     * 涉及到将基因编码到解空间，并评估解，获得得分，即为适应度
     *
     * @return
     */
    private double computeFitness() {
        int[] path = encodeGeneToPath();
        return -computeTravelLength(path);
    }

    /**
     * 将基因编码为旅行路线
     *
     * @return 旅行路线
     */
    public int[] encodeGeneToPath() {
        // n = nCity
        int n = genes.length;

        int[] path = new int[n];
        Arrays.fill(path, n);

        for (int i = 0; i < n; i++) {
            int k = genes[i] % n;
            while (path[k] != n) {
                // path[k] is occupied, find next empty slot
                k = (k + 1) % n;
            }
            path[k] = i;
        }

        return path;
    }

    /**
     * 计算旅行路线长度
     *
     * @param path 旅行的城市序列
     * @return 路线长度
     */
    public double computeTravelLength(int[] path) {
        double len = 0;
        int city1 = 0, city2 = 0;
        int n = path.length;
        for (int i = 0; i < n - 1; i++) {
            city1 = path[i];
            city2 = path[i + 1];

            len += problem.cost[city1][city2];
        }

        //make a cycle
        city1 = n - 1;
        city2 = 0;

        len += problem.cost[city1][city2];
        return len;
    }

    @Override
    public String toString() {
        int[] path = encodeGeneToPath();
        return MessageFormat.format("fit={0}, len={1}, gene={2}, path={3}", fitness(), computeTravelLength(path), Arrays.toString(genes), Arrays.toString(path));
    }
}
