package com.fireflyest.activity.view;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.bean.Rewards;
import com.fireflyest.activity.core.ActivityButton;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final Map<Integer, Reward> rewardMap = new HashMap<>();

    private ViewPage next = null;
    private ViewPage pre = null;

    public RewardPage(String title, String target, int page) {
        this.title = title;
        this.target = target;
        this.page = page;

        this.data = Activity.getData();

        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target) + "";    // 副标题
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码


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

        // 获取所有奖励
        List<Reward> allRewards = data.query(Reward.class);
        rewardMap.clear();
        rewardMap.putAll(allRewards.stream().collect(Collectors.toMap(Reward::getId, Function.identity(), (key1, key2) -> key2)));
        if (target.equals(RewardView.SIGN)){
            ItemStack bad = ActivityButton.BAD_REWARDS.clone();
            this.applyRewards(bad, RewardManager.getRewards(RewardManager.BAD));
            crashMap.put(45, bad);

            ItemStack sign = ActivityButton.SIGN_REWARDS.clone();
            this.applyRewards(sign, RewardManager.getRewards(RewardManager.SIGN));
            crashMap.put(46, sign);

            ItemStack series = ActivityButton.SERIES_REWARDS.clone();
            this.applyRewards(series, RewardManager.getRewards(RewardManager.SERIES));
            crashMap.put(47, series);

            ItemStack perfect = ActivityButton.PERFECT_REWARDS.clone();
            this.applyRewards(perfect, RewardManager.getRewards(RewardManager.PERFECT));
            crashMap.put(48, perfect);
        } else if (target.equals(RewardView.PLAYTIME)) {
            ItemStack tenM = ActivityButton.TEN_MINUTES_REWARDS.clone();
            this.applyRewards(tenM, RewardManager.getRewards(RewardManager.TEN_MINUTE));
            crashMap.put(45, tenM);

            ItemStack oneH = ActivityButton.ONE_HOURS_REWARDS.clone();
            this.applyRewards(oneH, RewardManager.getRewards(RewardManager.ONE_HOURS));
            crashMap.put(46, oneH);

            ItemStack threeH = ActivityButton.THREE_HOURS_REWARDS.clone();
            this.applyRewards(threeH, RewardManager.getRewards(RewardManager.THREE_HOURS));
            crashMap.put(47, threeH);

            ItemStack sixH = ActivityButton.SIX_HOURS_REWARDS.clone();
            this.applyRewards(sixH, RewardManager.getRewards(RewardManager.SIX_HOURS));
            crashMap.put(48, sixH);
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
        return target;
    }

    @Override
    public int getPage() {
        return page;
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

        if (target.equals(RewardView.SIGN)){
            itemMap.put(45, ActivityButton.BAD_REWARDS);
            itemMap.put(46, ActivityButton.SIGN_REWARDS);
            itemMap.put(47, ActivityButton.SERIES_REWARDS);
            itemMap.put(48, ActivityButton.PERFECT_REWARDS);
        } else if (target.equals(RewardView.PLAYTIME)) {
            itemMap.put(45, ActivityButton.TEN_MINUTES_REWARDS);
            itemMap.put(46, ActivityButton.ONE_HOURS_REWARDS);
            itemMap.put(47, ActivityButton.THREE_HOURS_REWARDS);
            itemMap.put(48, ActivityButton.SIX_HOURS_REWARDS);
        }

    }

    @Override
    public void updateTitle(String s) {

    }

    private void applyRewards(ItemStack item, Rewards rewards){
        if (rewards == null || rewards.noReward()) return;
        Set<Reward> rewardSet = new HashSet<>();
        for (Integer integer : rewards.getIntegerList()) rewardSet.add(rewardMap.get(integer));
        StringBuilder line = new StringBuilder("§f");
        int i = 0; // 列
        for (Reward reward : rewardSet) {
            line.append(String.format("%-8s", reward.getName()));
            i++;
            if (i%3 == 0) {
                i = 0;
                ItemUtils.addLore(item, line.toString());
                line = new StringBuilder("§f");
            }
        }
        ItemUtils.addLore(item, line.toString());
    }

}
