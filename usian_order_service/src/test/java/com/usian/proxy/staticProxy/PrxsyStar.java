package com.usian.proxy.staticProxy;

//代理角色
public class PrxsyStar implements Star {

    private Star star;

    public PrxsyStar(Star star) {
        super();
        this.star = star;
    }

    @Override
    public void confer() {
        System.out.println("PrxsyStar.confer");
    }

    @Override
    public void signContract() {
        System.out.println("PrxsyStar.signContract");
    }

    @Override
    public void bookTicket() {
        System.out.println("PrxsyStar.bookTicket");
    }

    @Override
    public void sing() {
        star.sing();
    }

    @Override
    public void collectMoney() {
        System.out.println("PrxsyStar.collectMoney");
    }
}
