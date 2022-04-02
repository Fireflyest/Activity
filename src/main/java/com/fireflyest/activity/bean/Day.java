package com.fireflyest.activity.bean;

/**
 * @author Fireflyest
 * 2022/2/25 20:45
 */

public class Day {

    private int id;

    private String owner;

    private int month;

    private int num;

    private boolean sign;

    private long playtime;

    public Day() {
    }

    public Day(String owner, int month, int num) {
        this.owner = owner;
        this.month = month;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }
}
