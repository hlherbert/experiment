package com.hl.javabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {
    public static void main(String[] args) {
        //测试iterator remove后，再把它加回去
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(1,2,3));

        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer i = it.next();
            it.remove();
            System.out.print("remove "+i);
            System.out.println(" "+list);


            list.add(i);
            System.out.println("add "+i);
            System.out.println(" " + list);
        }
    }
}
