package com.hl.designPattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// java dynamic proxy
public class JavaProxy implements InvocationHandler {

    Object target;

    public static void main(String[] args) {
        Cat cat = new Cat();
        JavaProxy proxy = new JavaProxy();
        ICat catProxy = (ICat) proxy.createProxyObject(cat);
        catProxy.miao();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("intercept at method: " + method);
        Object result = method.invoke(target, args);
        System.out.println("intercept end. return " + result);


        return result;
    }

    private Object createProxyObject(Object target) {
        this.target = target;
        //ClassLoader.getSystemClassLoader().loadClass();
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}
