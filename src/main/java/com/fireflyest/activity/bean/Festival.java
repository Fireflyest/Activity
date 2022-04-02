package com.fireflyest.activity.bean;

/**
 * @author Fireflyest
 * 2022/2/25 20:49
 */

public class Festival {

    private String name;

    private String display;

    private int month;

    private int day;

    private int reword;

    private int activity;

    private String item;

    public Festival(String name, int month, int day) {
        this.name = name;
        this.month = month;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getReword() {
        return reword;
    }

    public void setReword(int reword) {
        this.reword = reword;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
