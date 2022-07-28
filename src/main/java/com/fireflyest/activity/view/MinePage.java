package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.core.ActivityButton;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MinePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Inventory inventory;
    private final String target;

    private final Storage storage;

    private ViewPage next = null;
    private ViewPage pre = null;

    public MinePage(String title, String target) {
        this.storage = Activity.getStorage();
        this.target = target;

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题
        guiTitle += (" §7#§8个人信息");          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);
        int num = 0;
        for (int month = 1; month < 13 ; month++) {
            String condition = String.format(" where owner='%s' and month=%s", target, month);
            String sql;
            if(Config.SQL){
                sql = MysqlExecuteUtils.query(Day.class, condition);
            }else {
                sql = SqliteExecuteUtils.query(Day.class, condition);
            }
            List<Day> dayList = storage.inquiryList(sql, Day.class);
            if (dayList.size() == 0) continue;

            int signed = 0;
            long playtime = 0;
            for (Day day : dayList) {
                if (day.isSign()) signed++;
                playtime += day.getPlaytime();
            }

            ItemStack item = ActivityButton.getMonthButton(month).clone();
            ItemUtils.setLore(item, String.format("§3§l活跃天数§7: §f%s", dayList.size()), 0);
            ItemUtils.setLore(item, String.format("§3§l签到天数§7: §f%s", signed), 1);
            ItemUtils.setLore(item, String.format("§3§l在线时长§7: §f%s", TimeUtils.convertTime(playtime)), 2);
            crashMap.put(num++, item);
        }

//        List<Task> tasks = data.query(Task.class, 0, 44);
//        int i = 0, j = 45;
//        for (Task task : tasks) {
//            ItemStack item = SerializeUtil.deserialize(task.getStack(), task.getMeta());
//            ItemUtils.setDisplayName(item, "§e§l" + task.getName());
//            ItemUtils.addLore(item, "§7§m·                         ·");
//            ItemUtils.addLore(item, String.format("§3§lID§7: §f%s", task.getId()));
//            // 是否永久活动
//            long deadline = task.getDeadline();
//            if (deadline == 0){
//                crashMap.put(j, item);
//                j++;
//            }else {
//                ItemUtils.addLore(item, String.format("§3§l期限§7: §f%s", TimeUtils.getTime(deadline)));
//                crashMap.put(i, item);
//                i++;
//            }
//            ItemUtils.addLore(item, "§7" + task.getDesc());
//        }
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
        return 0;
    }

    @Override
    public ViewPage getNext() {
        return next;
    }

    @Override
    public ViewPage getPre() {
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
        for(int i = 18 ; i < 27 ; i++) itemMap.put(i, ActivityButton.BLANK);

        itemMap.put(53, ActivityButton.BACK);
    }

    @Override
    public void updateTitle(String s) {

    }

}
