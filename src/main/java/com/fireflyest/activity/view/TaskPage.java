package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Task;
import com.fireflyest.activity.core.ActivityButton;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.util.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class TaskPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

    private final Inventory inventory;
    private final String target;

    private final Data data;

    private ViewPage next = null;
    private ViewPage pre = null;

    public TaskPage(String title, String target) {
        this.data = Activity.getData();
        this.target = target;

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题
        guiTitle += (" §7#§8活动列表");          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
        List<Task> tasks = data.query(Task.class, 0, 44);
        int i = 0, j = 45;
        for (Task task : tasks) {
            ItemStack item = SerializeUtil.deserialize(task.getStack(), task.getMeta());
            ItemUtils.setDisplayName(item, "§e§l" + task.getName());
            ItemUtils.addLore(item, "§7§m·                         ·");
            ItemUtils.addLore(item, String.format("§3§lID§7: §f%s", task.getId()));
            // 是否永久活动
            long deadline = task.getDeadline();
            if (deadline == 0){
                itemStackMap.put(j, item);
                j++;
            }else {
                ItemUtils.addLore(item, String.format("§3§l期限§7: §f%s", TimeUtils.getTime(deadline)));
                itemStackMap.put(i, item);
                i++;
            }
            ItemUtils.addLore(item, "§7" + task.getDesc());
        }
        return itemStackMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
        return null;
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
        for(int i = 36 ; i < 45 ; i++) {
            itemMap.put(i, ActivityButton.BLANK);
        }
        itemMap.put(52, ActivityButton.BLANK);
        itemMap.put(53, ActivityButton.BACK);
    }

    @Override
    public void updateTitle(String s) {

    }

}
