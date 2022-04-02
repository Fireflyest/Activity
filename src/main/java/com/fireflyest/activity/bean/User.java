package com.fireflyest.activity.bean;

import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.util.TimeUtils;

/**
 * @author Fireflyest
 * 2022/2/25 19:25
 */

public class User {

    private String name;

    private String uuid;

    // 活跃度
    private int activity;

    // 在线时间
    private long playtime;

    // 补签机会
    private int chance;

    // 签到次数
    private int signed;

    // 连续签到
    private int series;

    // 最后签到时间
    private long lastSign;

    // 加入时间
    private long play;

    // 任务数量
    private int tasks;

    // 当前任务
    private int task;

    // 记录月份 用于给补签机会
    private int month;

    public User() {
    }

    public User(String name, String uuid, long play) {
        this.name = name;
        this.uuid = uuid;
        this.play = play;
        this.chance = Config.DEFAULT_CHANCE;
        this.month = TimeUtils.getMonth();
    }

    public long getLastSign() {
        return lastSign;
    }

    public void setLastSign(long lastSign) {
        this.lastSign = lastSign;
    }

    public long getPlay() {
        return play;
    }

    public void setPlay(long play) {
        this.play = play;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getSigned() {
        return signed;
    }

    public void setSigned(int signed) {
        this.signed = signed;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
