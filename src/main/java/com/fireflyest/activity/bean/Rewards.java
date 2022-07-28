package com.fireflyest.activity.bean;

import java.util.List;

/**
 * list of rewards
 *
 * @author Fireflyest
 * @version 1.0.0
 * @since 2022/7/28
 */
public class Rewards {

    private final String name;

    private final List<Integer> integerList;

    private final String msg;

    public Rewards(String name, List<Integer> integerList, String msg) {
        this.name = name;
        this.integerList = integerList;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void addReward(int id){
        this.integerList.add(id);
    }

    public boolean noReward(){
        if (integerList == null) return true;
        return integerList.size() <= 0;
    }

    public String getMsg() {
        return msg;
    }

}
