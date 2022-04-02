package com.fireflyest.activity.core;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Fireflyest
 * 2022/2/27 21:06
 */

public class XCalendar {

    public static Map<String,XDay> cache=new HashMap<>();

    private final static String[] chineseNumber = { "正", "二", "三", "四", "五", "六", "七",
            "八", "九", "十", "十一", "腊" };
    private final static String[] chineseNumber1 = { "一", "二", "三", "四", "五", "六", "七",
            "八", "九", "十", "十一", "十二" };
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
            0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
            0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
            0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
            0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
            0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
            0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
            0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
            0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
            0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
            0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
            0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
            0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
            0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
            0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
            0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
            0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
            0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
            0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
            0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

    private static final String[] solarTerm = new String[]{"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨",
            "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露",
            "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
    private static final int[] sTermInfo = new int[]{0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758};

    private static final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚",
            "辛", "壬", "癸" };
    private static final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午",
            "未", "申", "酉", "戌", "亥" };

    private static final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇",
            "马", "羊", "猴", "鸡", "狗", "猪" };

    private static final String[] chineseTen = { "初", "十", "廿", "卅" };

    //国历节日  *表示放假日
    private static final String[] sFtv = new String[]{
            "0101*元旦",

            "0214情人节",

            "0308妇女节",
            "0312植树节",

            "0401愚人节",
            "0422世界地球日",

            "0501*劳动节",
            "0504青年节",
            "0520情人节",

            "0601儿童节",

            "0701建党节",
            "0707抗日战争纪念日",

            "0801八一建军节",

            "0910教师节",
            "0917国际和平日",
            "0918九·一八事变纪念日",

            "1001*国庆节",
            "1010辛亥革命纪念日",
            "1031万圣节",

            "1111光棍节",

            "1212西安事变纪念日",
            "1213南京大屠杀",
            "1224平安夜",
            "1225圣诞节"};
    //农历节日  *表示放假日
    private static final String[] lFtv = new String[]{
            "0101*春节",
            "0102*大年初二",
            "0103*大年初三",
            "0104*大年初四",
            "0105*大年初五",
            "0106*大年初六",
            "0107*大年初七",
            "0115元宵节",
            "0202龙抬头",
            "0404寒食节",
            "0505*端午节",
            "0624彝族火把节",
            "0707七夕",
            "0714鬼节(南方)",
            "0815*中秋节",
            "0909重阳节",
            "1001祭祖节",
            "1208腊八节",
            "1223小年",
            "1229*腊月二十九",
            "1230*除夕"};


    // ====== 传回农历 y年的总天数
    private int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + this.leapDays(y));
    }

    // ====== 传回农历 y年闰月的天数
    private int leapDays(int y) {
        if (this.leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    // ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    private int leapMonth(int y) {
        return (int) (lunarInfo[y - 1900] & 0xf);
    }

    // ====== 传回农历 y年m月的总天数
    private int monthDays(int y, int m) {
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
            return 29;
        else
            return 30;
    }

    // ====== 传回农历 y年的生肖
    private String animalsYear(int year) {
        return Animals[(year - 4) % 12];
    }

    // ====== 传入 月日的offset 传回干支, 0=甲子
    private String cyclicalm(int num) {
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    private String dayOfWeek(int dow){
        switch (dow){
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    private String getChinaDayString(int day) {
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        if (day == 20)
            return "二十";
        if (day == 30)
            return "三十";
        return chineseTen[day / 10] + chineseNumber1[n];
    }

//    @Test
//    public void test(){
//        XCalendar calendar = new XCalendar();
//
//        XDay xDay = null;
//        try {
//            xDay = calendar.getXDay(2022, 2, 14);
//            System.out.println("calendar = " + xDay.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    //===== 某年的第n个节气为几日(从0小寒起算)
    private int sTerm(int  y,int  n) throws ParseException {
        long utcTime2 = simpleDateFormat.parse("1900-01-06 02:05:00").getTime();
        BigDecimal time2 = new BigDecimal("31556925974.7").multiply(new BigDecimal(y - 1900)).add(new BigDecimal( sTermInfo[n]).multiply(BigDecimal.valueOf(60000L)));
        BigDecimal time = time2.add(BigDecimal.valueOf(utcTime2));
        Date offDate = new Date(time.longValue());

        Calendar cal = Calendar.getInstance(Locale.CHINA) ;
        cal.setTime(offDate);
        //日期从0算起
        return cal.get(Calendar.DATE);
    }

    public XCalendar(){}

    /**
     * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
     * dayCyl5:与1900年1月31日相差的天数,再加40 ?
     *
     */
    public XDay getXDay(int sYear, int sMonth, int sDay) throws ParseException {
        String key = String.format("%s-%s-%s 08:00:00", sYear, sMonth, sDay);
        if (cache.containsKey(key)){
            return cache.get(key);
        }

        XDay xDay = new XDay(sYear, sMonth, sDay);
        Date baseDate = null, date = null;
        try {
            date = simpleDateFormat.parse(key);
            baseDate = simpleDateFormat.parse("1900-1-31 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace(); // To change body of catch statement use
        }
        
        if (baseDate == null || date == null) {
            return xDay;
        }

        // 星期几
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);

        boolean leap;
        int year, month, day, yearCyl, monCyl, dayCyl;
        int leapMonth;
        // 求出和1900年1月31日相差的天数
        int offset = (int) ((date.getTime() - baseDate.getTime()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;

        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }
        // 农历年份
        year = iYear;

        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        leap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else
                daysOfMonth = monthDays(year, iMonth);

            offset -= daysOfMonth;
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1))
                leap = false;
            if (!leap)
                monCyl++;
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        month = iMonth;
        day = offset + 1;
        
        // 国历节日
        for (String f : sFtv){
            if (Integer.parseInt(f.substring(0, 2)) == sMonth && sDay == Integer.parseInt(f.substring(2, 4))) {
                xDay.setSolarFestival(f.substring(4));
                break;
            }
        }
        // 农历节日
        for (String f : lFtv){
            boolean isThisMonth = Integer.parseInt(f.substring(0, 2)) == month;
            if (isThisMonth && day == Integer.parseInt(f.substring(2, 4))) {
                xDay.setLunarFestival(f.substring(4));
                break;
            }
            // 除夕
            if(month == 12 && isThisMonth && monthDays(year, month) == day){
                xDay.setLunarFestival("*除夕");
                break;
            }
        }
        
        // 每个月的两个节气
        int term1, term2;
        term1 = sTerm(sYear, (sMonth -1) * 2);
        term2 = sTerm(sYear, (sMonth -1) * 2 + 1);
        String termName1 = solarTerm[(sMonth -1) * 2];
        String termName2 = solarTerm[(sMonth -1) * 2 + 1];

        // 设置数据
        if(sDay == term1) {
            xDay.setSolarTerms(termName1);
        }else if(sDay == term2) {
            xDay.setSolarTerms(termName2);
        }
        xDay.setLeap(leap);
        xDay.setcYear(this.cyclicalm(yearCyl)+this.animalsYear(year) + "年");
        xDay.setcMonth(this.cyclicalm(monCyl) + "月");
        xDay.setcDay(this.cyclicalm(dayCyl) + "日");
        xDay.setcTime("农历" + (xDay.isLeap() ? "闰" : "") + chineseNumber[month - 1] + "月" + this.getChinaDayString(day));
        xDay.setDayOfWeek(this.dayOfWeek(dow));
        cache.put(key, xDay);
        return xDay;
    }



    public static class XDay{
        // 公历
        private final int year;
        private final int month;
        private final int day;
        private String dayOfWeek;
        // 农历
        private String cYear;
        private String cMonth;
        private String cDay;
        private String cTime;
        private boolean leap;
        // 节日
        private String lunarFestival;
        private String solarFestival;
        // 节气
        private String solarTerms;

        public XDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public String toString() {
            return "XDay{" +
                    "year=" + year +
                    ", month=" + month +
                    ", day=" + day +
                    ", dayOfWeek='" + dayOfWeek + '\'' +
                    ", cYear='" + cYear + '\'' +
                    ", cMonth='" + cMonth + '\'' +
                    ", cDay='" + cDay + '\'' +
                    ", cTime='" + cTime + '\'' +
                    ", leap=" + leap +
                    ", lunarFestival='" + lunarFestival + '\'' +
                    ", solarFestival='" + solarFestival + '\'' +
                    ", solarTerms='" + solarTerms + '\'' +
                    '}';
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getcYear() {
            return cYear;
        }

        public void setcYear(String cYear) {
            this.cYear = cYear;
        }

        public String getcMonth() {
            return cMonth;
        }

        public void setcMonth(String cMonth) {
            this.cMonth = cMonth;
        }

        public String getcDay() {
            return cDay;
        }

        public void setcDay(String cDay) {
            this.cDay = cDay;
        }

        public String getcTime() {
            return cTime;
        }

        public void setcTime(String cTime) {
            this.cTime = cTime;
        }

        public boolean isLeap() {
            return leap;
        }

        public void setLeap(boolean leap) {
            this.leap = leap;
        }

        public String getLunarFestival() {
            return lunarFestival;
        }

        public void setLunarFestival(String lunarFestival) {
            this.lunarFestival = lunarFestival;
        }

        public String getSolarFestival() {
            return solarFestival;
        }

        public void setSolarFestival(String solarFestival) {
            this.solarFestival = solarFestival;
        }

        public String getSolarTerms() {
            return solarTerms;
        }

        public void setSolarTerms(String solarTerms) {
            this.solarTerms = solarTerms;
        }
    }

}
