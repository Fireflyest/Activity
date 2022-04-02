package com.fireflyest.activity.bean;

/**
 * @author Fireflyest
 * 2022/2/25 20:52
 */

public class Reward {

    private int id;

    private String name;

    private double money;

    private String command;

    private String stack;
    private String meta;

    public Reward() {
    }

    public Reward(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
