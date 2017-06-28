package com.mstage.ignite;

public class Account {
    private int amount;
    private int id;

    public Account(int amount, int id) {
        this.amount = amount;
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
