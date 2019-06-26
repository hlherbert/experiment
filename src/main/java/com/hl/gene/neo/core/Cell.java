package com.hl.gene.neo.core;

import java.text.MessageFormat;

/**
 * 个体
 * 每个个体有若干个基因组成的基因序列genes
 * 基因编码为算法，解决特定任务，获得分数，即为个体的适应力fit（生命力）
 */
public interface Cell {

    /**
     * 随机初始化
     */
    void randomInit();

    /**
     * 求适应度
     * @return 适应度
     */
    double fitness();

    /**
     * 克隆（无性繁殖）
     * @return 克隆出的个体
     */
    Cell cloneCell();

    /**
     * 有性繁殖
     * 父母的基因复制、交叉，减数分裂
     * @param spouse 配偶
     * @return 有性繁殖产生的孩子
     */
    Cell birthBisex(Cell spouse);

    /**
     * 突变
     * 随机改变自身一个基因
     */
    void mutate();
}
