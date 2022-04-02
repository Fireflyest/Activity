package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.core.ActivityItem;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.util.ItemUtils;
import com.fireflyest.activity.util.SerializeUtil;
import com.fireflyest.gui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */
public class RewardPage implements ViewPage {

    private final Inventory inventory;

    private final Data data;

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

    private ViewPage next = null;
    private ViewPage pre = null;

    public RewardPage(String title, String target) {
        this.data = Activity.getData();

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
        List<Reward> rewards = data.query(Reward.class, 0, 36);
        int i = 0;
        for (Reward reward : rewards) {
            ItemStack item = SerializeUtil.deserialize(reward.getStack(), reward.getMeta());
            ItemUtils.addItemHide(item);
            ItemUtils.addLore(item, "§7§m·                         ·");
            ItemUtils.addItemData(item, "ID", reward.getId());
            ItemUtils.addItemData(item, "名称", reward.getName());
            itemStackMap.put(i, item);
            i++;
        }
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
        return null;
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
            itemMap.put(i, ActivityItem.BLANK);
        }
        itemMap.put(52, ActivityItem.BLANK);
        itemMap.put(53, ActivityItem.RETURN);

        itemMap.put(45, ActivityItem.BAD_REWARDS);
        itemMap.put(46, ActivityItem.SIGN_REWARDS);
        itemMap.put(47, ActivityItem.SERIES_REWARDS);
        itemMap.put(48, ActivityItem.PERFECT_REWARDS);
    }

}
