package com.hl.designPattern.observe.old;

import java.util.Observable;
import java.util.Observer;

public class MyPanel implements Observer {

    private static String nCopyString(String s, int n) {
        StringBuilder b = new StringBuilder(n * s.length());
        for (int i = 0; i < n; i++) {
            b.append(s);
        }
        return b.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        MyPanel myPanel = new MyPanel();
        MyGeom myGeom = new MyGeom();
        myGeom.addObserver(myPanel);

        for (int i = 0; i < 5; i++) {
            myGeom.setLen(myGeom.getLen() + 1);
            Thread.sleep(1000);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof MyGeom)) {
            return;
        }
        MyGeom geom = (MyGeom) o;
        int len = geom.getLen();
        System.out.println("geom changed! " + nCopyString("*", len));
    }
}
