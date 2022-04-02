package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityItem;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.XCalendar;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Storage;
import com.fireflyest.activity.util.ItemUtils;
import com.fireflyest.activity.util.MysqlExecuteUtils;
import com.fireflyest.activity.util.SqliteExecuteUtils;
import com.fireflyest.activity.util.TimeUtils;
import com.fireflyest.gui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MainPage implements ViewPage {

    public static final String FUTURE = "§f§l[§2§l待签§f§l]";
    public static final String TODAY = "§f§l[§a§l签到§f§l]";
    public static final String SIGNED = "§f§l[§e§l已签§f§l]";
    public static final String MISS = "§f§l[§c§l漏签§f§l]";

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final XCalendar calendar = new XCalendar();

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
    public Map<Integer, ItemStack> getItemMap(){
        // 数据库查询指令
        int today = TimeUtils.getDay();
        String condition = String.format(" where owner='%s' and month=%s", target, month);
        String sql;
        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Day.class, condition);
        }else {
            sql = SqliteExecuteUtils.query(Day.class, condition);
        }

        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
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
            XCalendar.XDay xDay = null;
            try {
                xDay = calendar.getXDay(year, month, d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 获取物品位置
            int index = k*9 + j;
            j++;
            if(j%9 == 7) {
                j = 0;
                k++;
            }
            ItemStack item;
            int thisMonth = TimeUtils.getMonth();

            if(iDay.isSign()){
                // 已签
                item = ActivityItem.SIGNED.clone();
                ItemUtils.setDisplayName(item, SIGNED);
            }else {
                if(thisMonth == month && d == today){
                    // 当天
                    item = ActivityItem.TODAY.clone();
                    ItemUtils.setDisplayName(item, TODAY);
                }else{
                    // 未签
                    item = ActivityItem.getNotSignItem(xDay);
                    if((thisMonth == month && d < today) || month < thisMonth){
                        ItemUtils.setDisplayName(item, MISS);
                    }else {
                        ItemUtils.setDisplayName(item, FUTURE);
                    }
                }
            }
            // 点击指令 只有本月可以签到
            if(thisMonth == month){
                ItemUtils.setItemValue(item, "/sign " + d);
            }
            // 设置物品样式
            if (xDay != null) {
                // 节日
                String f = "";
                if(xDay.getSolarFestival() != null){
                    f += ("§e§l" + xDay.getSolarFestival()).replace("*", "§6*§e§l");
                }
                if(xDay.getLunarFestival() != null){
                    f += ("§b§l" + xDay.getLunarFestival()).replace("*", "§6*§b§l");
                }
                if(xDay.getSolarTerms() != null){
                    f += ("§2§l" + xDay.getSolarTerms());
                }
                ItemUtils.addLore(item, f);
                // 日期
                ItemUtils.addLore(item, String.format("§f%s年%s月%s日 %s", year, month, d, xDay.getDayOfWeek()));
                ItemUtils.addLore(item, String.format("§f%s", xDay.getcTime()));
                ItemUtils.addLore(item, String.format("§7%s %s %s", xDay.getcYear(), xDay.getcMonth(), xDay.getcDay()));
            }

            // 日期展示成物品数量
            if(Config.DISPLAY_ITEM_NUMBER) item.setAmount(d);
            itemMap.put(index, item);
        }

        // 侧边导航
        User user = ActivityManager.getUser(target);

        ItemStack activity = ActivityItem.ACTIVITY.clone();
        ItemUtils.addItemData(activity, "活跃值", user.getActivity());
        ItemUtils.addLore(activity, "§f点击查看礼包");
        itemMap.put(8, activity);

        ItemStack tasks = ActivityItem.TASKS.clone();
        ItemUtils.addLore(tasks, "§f点击查看最新活动");
        itemMap.put(17, tasks);

        ItemStack playtime = ActivityItem.PLAYTIME.clone();
        ItemUtils.addItemData(playtime, "在线时长", TimeUtils.convertTime(ActivityManager.getOnlineTime(target)));
        ItemUtils.addItemData(playtime, "总在线", TimeUtils.convertTime(user.getPlaytime()+ActivityManager.getOnlineTime(target)));
        if(ActivityManager.hasTenMinuteReward(target)) {
            ItemUtils.addLore(playtime, "§f点击领取在线十分钟奖励");
        }
        if(ActivityManager.hasTwoHourReward(target)) {
            ItemUtils.addLore(playtime, "§f点击领取在线两小时奖励");
        }
        if(ActivityManager.hasSixHourReward(target)) {
            ItemUtils.addLore(playtime, "§f点击领取在线六小时奖励");
        }
        itemMap.put(26, playtime);

        ItemStack sign = ActivityItem.SIGN.clone();
        ItemUtils.addItemData(sign, "补签机会", user.getChance());
        ItemUtils.addItemData(sign, "连续签到", user.getSeries());
        ItemUtils.addItemData(sign, "累计签到", user.getSigned());
        ItemUtils.addLore(sign, "§f左右键切换月份");
        itemMap.put(35, sign);
        return itemStackMap;
    }

    @Override
    public Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    public Inventory getInventory(){
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
        for(int i = 7 ; i < 53 ; i+=9) {
            itemMap.put(i, ActivityItem.BLANK);
        }
        itemMap.put(44, ActivityItem.BLANK);
        itemMap.put(53, ActivityItem.CLOSE);
    }

}
