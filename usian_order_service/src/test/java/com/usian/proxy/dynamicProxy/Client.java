package com.usian.proxy.dynamicProxy;

import java.lang.reflect.Proxy;

public class Client {



    public static void main(String[] args) {
        Star star = new RealStar();

        ProxyStar proxyStar = new ProxyStar(star);

        //代理类对象
        Star o = (Star)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Star.class}, proxyStar);

        o.sing();
    }
}
