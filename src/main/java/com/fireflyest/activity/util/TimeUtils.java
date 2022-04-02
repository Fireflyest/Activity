package com.fireflyest.activity.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Fireflyest
 */
public class TimeUtils {

    private TimeUtils(){

    }

    /**
     * 获取当前年份
     * @return int
     */
    public static int getYear(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return int
     */
    public static int getMonth(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH)+1;
    }

    /**
     * 获取当天是当月第几天
     * @return int
     */
    public static int getDay(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     * @return int
     */
    public static int getHour(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.HOUR);
    }

    /**
     * 获取当前分钟
     * @return int
     */
    public static int getMinute(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒钟
     * @return int
     */
    public static int getSecond(){
        return Calendar.getInstance(Locale.CHINA).get(Calendar.SECOND);
    }

    public static String getTime(Date date){
        return getTime(date, "yyyy.MM.dd");
    }

    public static String getTime(long date){
        return getTime(new Date(date), "yyyy.MM.dd");
    }

    public static String getTime(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 由字符串获取时间
     * @param time 时间字符串
     * @return date
     * @throws ParseException 格式错误
     */
    public static Date getDate(String time) throws ParseException {
        return new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).parse(time);
    }

    /**
     * 获取当前日期字符串
     * @return String
     */
    public static String getTimeNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(Calendar.getInstance(Locale.CHINA).getTime());
    }

    /**
     * 获取当前时间数据
     * @return long
     */
    public static long getDate(){
        return Calendar.getInstance(Locale.CHINA).getTime().getTime();
    }


    /**
     * 该月最多有多少天
     * @return 天数
     */
    public static int getMaxDay(){
        return getMaxDay(getMonth());
    }

    public static int getMaxDay(int month){
        Calendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.set(Calendar.MONTH, month-1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取该月第一天为星期几
     * @return 星期几
     */
    public static int getFirstDay(){
        return getFirstDay(getMonth());
    }

    public static int getFirstDay(int month){
        Calendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month-1);
        return calendar.get(Calendar.DAY_OF_WEEK);
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
        if(day > 0){
            return day+"天"+hour+"时"+minute+"分"+second+"秒";
        }else if(hour > 0){
            return hour+"时"+minute+"分"+second+"秒";
        }else {
            return minute+"分"+second+"秒";
        }
    }

}
