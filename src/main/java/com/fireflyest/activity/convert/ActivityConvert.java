package com.fireflyest.activity.convert;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Fireflyest
 * ❤✦✧@❀❁❂☻☠☽⚒⚓⚔⚜⚝✈✂✄✘♯¶♩♪♫♬
 */
public class ActivityConvert {

    private final static String BAR = "▎";

    private ActivityConvert(){

    }

    /**
     * 获取一定数量的符号条
     * @param amount 数量
     * @return String
     */
    public static String convertBar(double amount) {
        String bars = "";
        for(int i = (int)amount ; i > 0 ; i--) { bars = bars.concat(BAR); }
        return bars;
    }

    /**
     * 将long类型转化为时间
     * @param time 时间数据
     * @return String
     */
    public static String convertTime(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = time / dd;
        long hour = (time - day * dd)/ hh;
        long minute = (time - day * dd - hour * hh )/ mi;
        long second = (time - day * dd - hour * hh - minute * mi )/ ss;
        if(time < 60000)return second+"秒";
        if(time < 3600000)return minute+"分"+second+"秒";
        if(time < 86400000)return hour+"时"+minute+"分"+second+"秒";
        return day+"天"+hour+"时"+minute+"分"+second+"秒";
    }

    /**
     * 将String类型时间转化为long
     * @param time 时间
     * @return date
     */
    public static long convertTime(String time) {
        long date = 1;
        if(time.contains("M")) { date = 1000*60; date *= Integer.parseInt(time.replace("M", "")); 	}
        if(time.contains("H")) { date = 1000*60*60; date *= Integer.parseInt(time.replace("H", "")); }
        if(time.contains("D")) { date = 1000*60*60*24; date *= Integer.parseInt(time.replace("D", "")); }
        return date;
    }

    /**
     * 将位置转化为字符串
     * @param loc 位置
     * @return String
     */
    public static String convertLocation(Location loc){
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    /**
     * 将字符串数据转化为位置
     * @param loc 字符串位置数据
     * @return Location
     */
    public static Location convertLocation(String loc){
        String[] l = loc.split(",");
        return new Location(Bukkit.getWorld(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]), Float.parseFloat(l[4]), Float.parseFloat(l[5]));
    }

}
