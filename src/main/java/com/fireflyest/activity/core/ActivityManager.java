package com.fireflyest.activity.core;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Storage;
import com.fireflyest.activity.util.MysqlExecuteUtils;
import com.fireflyest.activity.util.SqliteExecuteUtils;
import com.fireflyest.activity.util.TimeUtils;
import org.fireflyest.craftgui.api.ViewGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/3/2 12:14
 */

public class ActivityManager {

    private static final Map<String,Long> joinTime = new HashMap<>();

    private static final List<String> tenMinuteReward = new ArrayList<>();
    private static final List<String> twoHourReward = new ArrayList<>();
    private static final List<String> sixHourReward = new ArrayList<>();

    private static ViewGuide guide;
    private static Data data;
    private static Storage storage;

    private ActivityManager(){
    }

    public static void initActivityManager(){
        guide = Activity.getGuide();
        data = Activity.getData();
        storage = Activity.getStorage();

    }

    public static User getUser(String playerName){
        return data.queryOne(User.class, "name", playerName);
    }

    public static void addUser(User user){
        data.insert(user);
    }

    public static Day getDay(String playerName){
        return getDay(playerName, TimeUtils.getMonth(), TimeUtils.getDay());
    }

    public static Day getDay(String playerName, int month, int day){
        String condition = String.format(" where owner='%s' and month=%s and num=%s", playerName, month, day);
        String sql;
        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Day.class, condition);
        }else {
            sql = SqliteExecuteUtils.query(Day.class, condition);
        }
        Day aDay = storage.inquiry(sql, Day.class);
        if (aDay == null) {
            aDay = new Day(playerName, month, day);
            data.insert(aDay);
        }
        return aDay;
    }

    public static void playerJoin(String name){
        joinTime.put(name, TimeUtils.getDate());
    }

    public static void playerQuit(String playerName){
        User user = getUser(playerName);
        Day day = getDay(playerName);
        user.setPlaytime(user.getPlaytime() + getOnlineTime(playerName));
        day.setPlaytime(day.getPlaytime() + getOnlineTime(playerName));
        data.update(user);
        data.update(day);
        joinTime.remove(playerName);
    }

    public static long getOnlineTime(String playerName){
        return TimeUtils.getDate() - joinTime.get(playerName);
    }

    public static long getTodayOnlineTime(String playerName){
        Day day = getDay(playerName);
        return day.getPlaytime() + TimeUtils.getDate() - joinTime.get(playerName);
    }

    public static Reward getReward(int id){
        return data.queryOne(Reward.class, "id", id);
    }

    public static boolean hasTenMinuteReward(String playerName){
        return  (getOnlineTime(playerName) > 1000*60*10 && !tenMinuteReward.contains(playerName));
    }

    public static boolean hasTwoHourReward(String playerName){
        return  (getOnlineTime(playerName) > 1000*60*60*2 && !twoHourReward.contains(playerName));
    }

    public static boolean hasSixHourReward(String playerName){
        return  (getOnlineTime(playerName) > 1000*60*60*6 && !sixHourReward.contains(playerName));
    }

    public static void ridTenMinuteReward(String playerName){
        tenMinuteReward.add(playerName);
    }

    public static void ridTwoHourReward(String playerName){
        twoHourReward.add(playerName);
    }

    public static void ridSixHourReward(String playerName){
        sixHourReward.add(playerName);
    }

}
