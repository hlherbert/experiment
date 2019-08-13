package com.hl.algo.dividen;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

/**
 * 分形图案
 */
public class DividenPanel extends JPanel {

    private int MAX_DEPTH = 3;
    private boolean overlap = false;
    private boolean[] b=new boolean[4];
    private List<Shape> shapes = new LinkedList<Shape>();

    private DividenPanel() {
        init();
    }

    private void dividenFunc(int depth, float x, float y, float w, float h) {
        if (depth < 0) {
            return;
        }

        if (overlap || depth==0) {
            if (b[0]) {
                Shape line = new Line2D.Float(x, y, x + w, y + h);
                shapes.add(line);
            }
            if (b[1]) {
                Shape line = new Line2D.Float(x, y + h, x + w, y);
                shapes.add(line);
            }
            if (b[2]) {
                Rectangle2D.Float rect = new Rectangle2D.Float(x + w / 4, y + h / 4, w / 2, h / 2);
                shapes.add(rect);
            }
            if (b[3]) {
                Eclipse2D.Float eclipse = new Eclipse2D.Float(x+w/2,y+h/2,w/2, h/2);
                shapes.add(eclipse);
            }
        }

        depth--;
        float dw = w/2;
        float dh = h/2;
        dividenFunc(depth,x,y,dw,dh);
        dividenFunc(depth,x+dw,y,dw,dh);
        dividenFunc(depth,x,y+dh,dw,dh);
        dividenFunc(depth,x+dw,y+dh,dw,dh);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g1 = (Graphics2D)g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = this.getWidth();
        int h = this.getHeight();
        shapes.clear();
        dividenFunc(MAX_DEPTH,0,0,w,h);
        for (Shape shape: shapes) {
            g1.draw(shape);
        }
    }

    public void init() {
        JButton btn = new JButton("+depth");
        this.add(btn);
        btn.addActionListener((e)->{
            MAX_DEPTH++;
            this.repaint();
        });

        JButton btn2 = new JButton("-depth");
        this.add(btn2);
        btn2.addActionListener((e)->{
            if (MAX_DEPTH > 0)
                MAX_DEPTH--;
            this.repaint();
        });

        JButton btn3 = new JButton("overlap");
        this.add(btn3);
        btn3.addActionListener((e)->{
            overlap = !overlap;
            this.repaint();
        });

        for (int i=0;i<b.length;i++) {
            JButton btnShape = new JButton(Integer.toString(i));

            this.add(btnShape);
            btnShape.addActionListener((e)->{
                int index = Integer.valueOf(btnShape.getText());
                b[index] = !b[index];
                this.repaint();
            });
        }
    }

    public static void main(String[] args) {
        JFrame frm = new JFrame("Dividen");
        frm.setSize(500,500);
        DividenPanel dividenPanel = new DividenPanel();
        frm.setContentPane(dividenPanel);
        frm.setVisible(true);
    }
}
