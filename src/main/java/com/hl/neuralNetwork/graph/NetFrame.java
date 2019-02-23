package com.hl.neuralNetwork.graph;

import com.hl.neuralNetwork.util.ClassifySample;

import javax.swing.*;
import java.awt.*;


/**
 * 神经网络画布
 */
public class NetFrame extends JFrame {

    private static final int sx = 50;//小方格宽度
    private static final int sy = 50;//小方格高度
    private static final int w = 40;
    private static final int rw = 400;


    private Graphics jg;


    private Color rectColor = new Color(0xf5f5f5);

    /**
     * DrawSee构造方法
     */
    public NetFrame() {
        Container p = getContentPane();
        setBounds(100, 100, 500, 500);
        setVisible(true);
        p.setBackground(rectColor);
        setLayout(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {


            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取专门用于在窗口界面上绘图的对象
        jg = this.getGraphics();

        // 绘制游戏区域
        //paintComponents(jg);
    }


    public void paintComponents(Graphics g) {
        try {
            // 设置线条颜色为红色
            g.setColor(Color.RED);

            // 绘制外层矩形框
            g.drawRect(sx, sy, rw, rw);

            /* 绘制水平10个，垂直10个方格。
             * 即水平方向9条线，垂直方向9条线，
             * 外围四周4条线已经画过了，不需要再画。
             * 同时内部64个方格填写数字。
             */
            for (int i = 1; i < 10; i++) {
                // 绘制第i条竖直线
                g.drawLine(sx + (i * w), sy, sx + (i * w), sy + rw);

                // 绘制第i条水平线
                g.drawLine(sx, sy + (i * w), sx + rw, sy + (i * w));

                // 填写第i行从第1个方格到第8个方格里面的数字（方格序号从0开始）
                for (int j = 0; j < 10; j++) {
                    //drawString(g, j, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void paintClassifiedSamples(java.util.List<ClassifySample> samples) {
        Graphics g = this.getGraphics();
        try {
            // 设置线条颜色为红色
            g.setColor(Color.RED);

            Color[] colors = {Color.red, Color.black, Color.yellow, Color.blue};

            for (ClassifySample sample:samples) {
                Color color = colors[sample.klass];
                g.setColor(color);
                int x = (int)(100*sample.v.x[0]);
                int y = (int)(100*sample.v.x[1]);
                g.drawRect(x, y, 2, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}