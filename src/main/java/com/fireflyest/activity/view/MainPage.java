package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityButton;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.ActivityCalendar;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Storage;
import com.fireflyest.activity.util.MysqlExecuteUtils;
import com.fireflyest.activity.util.SqliteExecuteUtils;
import com.fireflyest.activity.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MainPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();
    private final ActivityCalendar calendar = new ActivityCalendar();

    private final Inventory inventory;
    private final String title;
    private final String target;
    private final int year;
    private final int month;

    private final Storage storage;

    private ViewPage next = null;
    private ViewPage pre = null;

    public MainPage(String title, String target, int year, int month) {
        this.storage = Activity.getStorage();
        this.title = title;
        this.target = target;
        this.year = year;
        this.month = month;

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题
        guiTitle += (" §7#§8" + month + "月");          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        // 数据库查询指令
        int today = TimeUtils.getDay();
        int thisMonth = TimeUtils.getMonth();
        String condition = String.format(" where owner='%s' and month=%s", target, month);
        String sql;
        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Day.class, condition);
        }else {
            sql = SqliteExecuteUtils.query(Day.class, condition);
        }

        crashMap.clear();
        crashMap.putAll(itemMap);

        // 将每天按数字放
        List<Day> days = storage.inquiryList(sql, Day.class);
        Map<Integer, Day> dayMap = new HashMap<>();
        for (Day d : days) {
            dayMap.put(d.getNum(), d);
        }
        // 放置物品
        int k = 0, j = TimeUtils.getFirstDay(month)-1;
        for (int i = 0; i < TimeUtils.getMaxDay(month); i++) {
            int d = i+1;
            // 获取当天数据
            Day iDay = dayMap.getOrDefault(d, new Day(target, month, d));
            ActivityCalendar.Day xDay = null;
            try {
                xDay = calendar.getXDay(year, month, d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 获取物品位置
            int index = k*9 + j++;
            if(j%9 == 7) {
                j = 0;
                k++;
            }
            ItemStack item;
            if(iDay.isSign()){
                item = ActivityButton.SIGNED.clone(); // 已签
            }else {
                if(thisMonth == month && d == today){
                    item = ActivityButton.TODAY.clone(); // 当天
                }else{
                    item = ActivityButton.getNotSignItem(xDay, (thisMonth == month && d < today) || month < thisMonth); // 未签
                }
            }
            // 展示天数(一些节日物品可能无法正常显示，例如鸡蛋最多叠16个)
            if(Config.DISPLAY_ITEM_NUMBER) item.setAmount(d);
            // 点击指令 只有本月可以签到
            if(thisMonth == month) ItemUtils.setItemValue(item, "sign " + d);
            // 设置物品样式
            if (xDay != null) {
                // 节日
                String f = "";
                if(xDay.getSolarFestival() != null) f += ("§e§l" + xDay.getSolarFestival()).replace("*", "§6*§e§l");
                if(xDay.getLunarFestival() != null) f += ("§b§l" + xDay.getLunarFestival()).replace("*", "§6*§b§l");
                if(xDay.getSolarTerms() != null) f += ("§2§l" + xDay.getSolarTerms());
                ItemUtils.addLore(item, f);
                // 日期
                ItemUtils.addLore(item, String.format("§f%s年%s月%s日 %s", year, month, d, xDay.getDayOfWeek()));
                ItemUtils.addLore(item, String.format("§f%s", xDay.getcTime()));
                ItemUtils.addLore(item, String.format("§7%s %s %s", xDay.getcYear(), xDay.getcMonth(), xDay.getcDay()));
            }

            crashMap.put(index, item);
        }

        if (thisMonth != month){
            return crashMap;
        }

        // 侧边导航
        User user = ActivityManager.getUser(target);
        ItemStack activity = crashMap.get(8);
        ItemStack playtime = crashMap.get(17);
        ItemStack sign = crashMap.get(26);

        ItemUtils.setLore(activity, String.format("§3§l活跃值§7: §f%s", user.getActivity()), 0);

        ItemUtils.setLore(playtime, String.format("§3§l持续在线§7: §f%s", TimeUtils.convertTime(ActivityManager.getOnlineTime(target))), 0);
        ItemUtils.setLore(playtime, String.format("§3§l今日在线§7: §f%s", TimeUtils.convertTime(ActivityManager.getTodayOnlineTime(target))), 1);
        ItemUtils.setLore(playtime, String.format("§3§l总在线§7: §f%s", TimeUtils.convertTime(user.getPlaytime()+ActivityManager.getOnlineTime(target))), 2);
        if(ActivityManager.hasTenMinuteReward(target)) {
            ItemUtils.setLore(playtime, "§fShift+点击领取在线十分钟奖励", 3);
        }else if(ActivityManager.hasOneHourReward(target)) {
            ItemUtils.setLore(playtime, "§fShift+点击领取在线一小时奖励", 3);
        }else if(ActivityManager.hasThreeHourReward(target)) {
            ItemUtils.setLore(playtime, "§fShift+点击领取在线三小时奖励", 3);
        }else if(ActivityManager.hasSixHourReward(target)) {
            ItemUtils.setLore(playtime, "§fShift+点击领取在线六小时奖励", 3);
        }

        ItemUtils.setLore(sign, String.format("§3§l补签机会§7: §f%s", user.getChance()), 0);
        ItemUtils.setLore(sign, String.format("§3§l连续签到§7: §f%s", user.getSeries()), 1);
        ItemUtils.setLore(sign, String.format("§3§l累计签到§7: §f%s", user.getSigned()), 2);
        return crashMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
        return crashMap.get(i);
    }

    @Override
    public @NotNull Inventory getInventory(){
        return inventory;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getPage() {
        return month;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && month < 12){
            next = new MainPage(title, target, year,month+1);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public ViewPage getPre() {
        if(pre == null && month > 1){
            pre = new MainPage(title, target, year,month-1);
            pre.setNext(this);
        }
        return pre;
    }

    @Override
    public void setPre(ViewPage pre) {
        this.pre = pre;
    }

    @Override
    public void setNext(ViewPage next) {
        this.next = next;
    }

    @Override
    public void refreshPage() {
        for(int i = 7 ; i < 53 ; i+=9) itemMap.put(i, ActivityButton.BLANK);

        itemMap.put(8, ActivityButton.ACTIVITY.clone());
        itemMap.put(35, ActivityButton.MONTH.clone());
        itemMap.put(53, ActivityButton.CLOSE);

        if (month == TimeUtils.getMonth()){
            itemMap.put(17, ActivityButton.PLAYTIME.clone());
            itemMap.put(26, ActivityButton.SIGN.clone());
        }
    }

    @Override
    public void updateTitle(String s) {

    }

}
