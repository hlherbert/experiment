package com.hl.proxy;


public class Cat implements ICat{

    @Override
    public void miao(){
        System.out.println("I am a cat! Miao!");
    }

    private void wang() {
        System.out.println("I am cat, Wang!");
    }
}
