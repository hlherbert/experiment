package com.hl.gene.neo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * 基因算法，简化版本，重写
 * (1)初始阶段： initPopulation
 * 1. 生成第一代，初始 nPopulation 个个体
 * 2. 每个个体的基因随机
 * 3. 基因编码为算法，解决特定任务，获得分数，即为个体的适应力fit（生命力）
 *
 * (2)繁殖阶段： birth
 * 4. 复制：
 * 无性：每个个体复制出同样的下一代。   birthUnisex
 * 有性：个体两两组合，复制出两个后代。两个后代的基因随机选择和交叉   birthBisex
 *
 * 5.突变：下一代中，随机在 mutationRate% 个体中产生突变，改变任意一个基因  mutate
 *
 * (3)淘汰阶段： die
 * 6. 适应力前50%的个体保留下来。
 *
 * (4)下一代：
 * 7. 生命周期 cycle <- cycle+1. 繁殖阶段和淘汰阶段，直到达到最大生命周期nMaxCycle。
 *
 * (5)结果：
 * 8. 适应力最强的个体即为已知最优解。
 */
public class NeoGA {

    private static final Logger logger = LoggerFactory.getLogger(NeoGA.class);

    /**
     * 运行的轮数，多轮运行，每次随机初始化，防止陷在局部最优解当中
     */
    private int nRun = 5;

    /**
     * 总个体数
     */
    private int nPopulation = 1000;

    /**
     * 生命周期总数（产生的最大代数）
     */
    private int nMaxCycle = 1000;

    /**
     * 突变率（0~1）, 下一代个体，有mutationRate概率会发生突变
     */
    private double mutationRate = 1e-3;

    /**
     * 个体工厂
     */
    private CellFactory cellFactory;

    /**
     * 所有个体
     */
    private Cell[] cells;

    private Random crossOverRandom = new Random();
    /**
     * 变异随机发生器
     */
    private Random mutationRandom = new Random();

    /**
     * 参数设置
     */
    public void setParams(CellFactory cellFactory, int population, int maxCycle, double mutationRate, int nRun) {
        this.cellFactory = cellFactory;
        this.nPopulation = population;
        this.nMaxCycle = maxCycle;
        this.mutationRate = mutationRate;
        this.nRun = nRun;

        logger.info("NeoGA params: nRun={}, population={}, maxCycle={}, mutationRate={}", nRun, nPopulation, nMaxCycle, mutationRate);
    }

    /**
     * 运行进化算法, 返回最适应的个体
     */
    public Cell perform() {
        Cell bestCellInAllRun = null;
        for (int r=0;r<nRun;r++) {
            initPopulation();

            // bestCell in this run
            Cell bestCell = fitestCell();
            for (int cycle=0;cycle<nMaxCycle;cycle++) {
                birth();
                die();

                bestCell = fitestCell();
                if (cycle==nMaxCycle-1) {
                    logger.info("gen={}, best={}", cycle, bestCell);
                }
            }

            if (bestCellInAllRun == null) {
                bestCellInAllRun = bestCell;
            } else if (bestCell.fitness() > bestCellInAllRun.fitness()) {
                bestCellInAllRun = bestCell;
            }
        }

        return bestCellInAllRun;
    }

    /**
     * 生成第一代个体，初始 n 个个体
     */
    private void initPopulation() {
        cells = new Cell[2*nPopulation];
        // 个体空间分为两半：
        // 0 ~ n-1  : 本代个体
        // n ~ 2n-1 : 下一代个体
        for (int i=0;i<nPopulation;i++) {
            cells[i] = cellFactory.createCell();
            cells[i].randomInit();
        }
    }

    /**
     * 繁殖阶段
     */
    private void birth() {
        // 无性繁殖的问题：容易使种群很快单一化，趋同，不再变化。
        // 有性繁殖效果更容易使下一代更多样化，利于寻找全局解
        // birthUnisex();
        birthBisex();
        mutate();
    }

    /**
     * 无性繁殖
     */
    private void birthUnisex() {
        //每个个体复制自身成为下一代
        for (int i=0;i<nPopulation;i++) {
            cells[i+nPopulation] = cells[i].cloneCell();
        }
    }

    /**
     * 有性繁殖
     */
    private void birthBisex() {
        // 对本代每个个体，任选一个配偶，然后进行分裂、交叉，产生下一代个体
        for (int i=0;i<nPopulation;i++) {
            Cell parent1 = cells[i];
            int j = crossOverRandom.nextInt(nPopulation);
            while (i == j) {
                j = crossOverRandom.nextInt(nPopulation);
            }
            Cell parent2 = cells[j];

            Cell child = parent1.birthBisex(parent2);
            cells[i+nPopulation] = child;
        }
    }

    /**
     * 基因突变
     */
    private void mutate() {
        for (int i=nPopulation;i<2*nPopulation;i++) {
            // 每个个体具有mutationRate的概率变异
            if (mutationRandom.nextDouble()<mutationRate) {
                cells[i].mutate();
            }
        }
    }

    /**
     * 淘汰阶段
     */
    private void die() {
        // 适应力前50%(nPopulation)个体保留下来，其余的死去
        Arrays.sort(cells, Comparator.comparing(Cell::fitness).reversed());
        for (int i=nPopulation;i<2*nPopulation;i++) {
            cells[i] = null;
        }
    }

    /**
     * 返回最适应的个体
     * @return 最适应的个体，代表最优解
     */
    private Cell fitestCell() {
        assert cells.length>0;
        return cells[0];
    }
}
