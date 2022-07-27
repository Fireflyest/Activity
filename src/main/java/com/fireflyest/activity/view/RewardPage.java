package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.core.ActivityButton;
import com.fireflyest.activity.data.Data;
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
public class RewardPage implements ViewPage {

    private final Inventory inventory;

    private final String title;
    private final String target;
    private final int page;
    private final Data data;

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private ViewPage next = null;
    private ViewPage pre = null;

    public RewardPage(String title, String target, int page) {
        this.title = title;
        this.target = target;
        this.page = page;

        this.data = Activity.getData();

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        List<Reward> rewards = data.query(Reward.class,  (page-1)*33, page*33);
        int i = 0;
        for (Reward reward : rewards) {
            ItemStack item = SerializeUtil.deserialize(reward.getStack(), reward.getMeta());
            ItemUtils.addLore(item, "§7§m·                         ·");
            ItemUtils.addLore(item, String.format("§3§lID§7: §f%s", reward.getId()));
            ItemUtils.addLore(item, String.format("§3§l名称§7: §f%s", reward.getName()));
            ItemUtils.setItemValue(item, "reward " + reward.getId());
            crashMap.put(i, item);
            i++;
        }
        if (i < 33) crashMap.put(i, ActivityButton.ADD_REWARD);
        for (i++ ; i < 33; i++) {
            crashMap.put(i, ActivityButton.AIR);
        }

        // 可以下一页
        if (rewards.size() != 0){
            crashMap.put(35, ActivityButton.PAGE_NEXT);
        }

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
        return null;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 10){
            next = new RewardPage(title, target, page+1);
            next.setPre(this);
        }
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
        for(int i = 36 ; i < 45 ; i++) itemMap.put(i, ActivityButton.BLANK);
        itemMap.put(53, ActivityButton.BACK);

        // 上一页
        if (page == 1){
            itemMap.put(34, ActivityButton.PAGE_PRE_DISABLE);
        }else {
            itemMap.put(34, ActivityButton.PAGE_PRE);
        }
        // 下一页
        itemMap.put(35, ActivityButton.PAGE_NEXT_DISABLE);

        itemMap.put(45, ActivityButton.BAD_REWARDS);
        itemMap.put(46, ActivityButton.SIGN_REWARDS);
        itemMap.put(47, ActivityButton.SERIES_REWARDS);
        itemMap.put(48, ActivityButton.PERFECT_REWARDS);
    }

    @Override
    public void updateTitle(String s) {

    }

}
