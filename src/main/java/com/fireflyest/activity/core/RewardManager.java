package com.fireflyest.activity.core;

import com.cryptomorin.xseries.XSound;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.bean.Rewards;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.util.SerializeUtil;

import java.util.*;

/**
 * @author Fireflyest
 * 2022/3/5 20:05
 */

public class RewardManager {

    private static FileConfiguration config;

    private static final Map<String, Rewards> rewardMap = new HashMap<>();
    public static final String SIGN = "SignRewards";
    public static final String BAD = "BadRewards";
    public static final String SERIES = "SeriesRewards";
    public static final String PERFECT = "PerfectRewards";
    public static final String TEN_MINUTE = "TenMinutesRewards";
    public static final String ONE_HOURS = "OneHoursRewards";
    public static final String THREE_HOURS = "ThreeHoursRewards";
    public static final String SIX_HOURS = "SixHoursRewards";
    public static final String QUIZ = "QuizRewards";

    private static final Sound rewardSound = XSound.ENTITY_PLAYER_LEVELUP.parseSound();

    private RewardManager(){
    }

    public static void setupRewards(){
        config = YamlUtils.getConfig();

        ConfigurationSection rewards = config.getConfigurationSection("Rewards");
        if (rewards == null) return;
        for (String key : rewards.getKeys(false)) {
            List<Integer> integerList = rewards.getIntegerList(String.format("%s.IDList", key));
            String msg = rewards.getString(String.format("%s.Message", key));
            rewardMap.put(key, new Rewards(key, integerList, msg));
        }
    }

    public static void addReward(String type, int id){
        // 修改缓存
        if (rewardMap.containsKey(type))  rewardMap.get(type).addReward(id);
        // 修改配置
        String key = String.format("Rewards.%s.IDList", type);
        List<Integer> rewards = config.getIntegerList(key);
        rewards.add(id);
        YamlUtils.setConfigData(key, rewards);
    }

    public static Rewards getRewards(String type){
        return rewardMap.get(type);
    }

    public static Map<String, Rewards> getRewardMap() {
        return rewardMap;
    }

    public static void giveReward(Player player, String type){
        Random random = new Random();
        // 获取奖励id列表
        if (SIGN.equals(type)) type = new Date().getTime() % 7 == 0 ? BAD : SIGN;
        Rewards rewards = rewardMap.get(type);
        // 判断是否有奖励
        if(rewards == null || rewards.noReward()){
            player.sendMessage(Language.TITLE + "暂无奖励");
            return;
        }
        List<Integer> ids = rewards.getIntegerList();
        // 随机获取奖品
        int index = random.nextInt(ids.size());
        int id = ids.get(index);
        Reward reward = ActivityManager.getReward(id);
        if (reward == null) {
            player.sendMessage(Language.TITLE + "奖励错误null");
            return;
        }
        // 判断是指令还是物品
        if(reward.getCommand() != null && !"null".equals(reward.getCommand()) && !"".equals(reward.getCommand())){
            String command = reward.getCommand().replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }else {
            player.getInventory().addItem(SerializeUtil.deserialize(reward.getStack(), reward.getMeta()));
        }
        // 奖励提示
        if (rewards.getMsg() != null) {
            player.sendMessage(rewards.getMsg().replace("&", "§").replace("%reward%", reward.getName()));
        }
        // 奖励声音
        if (rewardSound != null) player.playSound(player.getLocation(), rewardSound, 1F, 1F);
    }

}
