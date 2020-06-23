package com.usian.proxy.staticProxy;

//实现类
public class Client {
    public static void main(String[] args) {
        Star star = new RealStar();
        Star prxsy = new PrxsyStar(star);

        prxsy.bookTicket();
        prxsy.sing();
    }
}
