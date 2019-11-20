package com.hl.designPattern.observe;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

public class ShapePanel extends JPanel implements MyObserver {

    private MyShape myShape;
    private int len = 0;

    public ShapePanel(MyShape myShape) {
        this.myShape = myShape;

        myShape.addObserver(this);
        this.onObservableChanged(myShape);
    }

    public static void main(String[] args) {
        MyShape myShape = new MyShape();

        JFrame jFrame = new JFrame();
        jFrame.setContentPane(new ShapePanel(myShape));

        JButton btn = new JButton("length++");
        btn.addActionListener(e -> {
            myShape.setLength(myShape.getLength() + 10);
        });
        jFrame.getContentPane().add(btn);

        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    @Override
    public void onObservableChanged(MyObservable observable) {
        MyShape shape = (MyShape) observable;
        len = shape.getLength();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(10, 10, 10 + len, 10 + len);
    }
}
