package com.usian.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//通过invoke调用真实角色
public class ProxyStar implements InvocationHandler {

    private Object realStar;

    public ProxyStar(Object object){
        this.realStar = object;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前附加方法");
        Object result = method.invoke(realStar, args);
        System.out.println("后附加方法");
        return result;
    }
}
