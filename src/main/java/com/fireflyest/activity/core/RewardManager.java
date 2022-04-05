package com.fireflyest.activity.core;

import com.cryptomorin.xseries.XSound;
import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.SerializeUtil;
import com.fireflyest.activity.util.TimeUtils;
import com.fireflyest.activity.util.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Fireflyest
 * 2022/3/5 20:05
 */

public class RewardManager {

    public static final FileConfiguration config = YamlUtils.getConfig();

    public static final int SIGN = 1;
    public static final int SERIES = 2;
    public static final int PERFECT = 3;
    public static final int TEN_MINUTE = 4;
    public static final int TWO_HOURS = 5;
    public static final int SIX_HOURS = 6;
    public static final int QUIZ = 7;

    private static final Sound rewardSound = XSound.ENTITY_PLAYER_LEVELUP.parseSound();

    private RewardManager(){
    }

    public static void addReward(String type, int id){
        List<Integer> rewards = config.getIntegerList(type);
        rewards.add(id);
        YamlUtils.setConfigData(type, rewards);
    }

    public static void giveReward(Player player, int type){
        List<Integer> rewards = null;
        Random random = new Random();
        switch (type){
            case SIGN:
                rewards = new Date().getTime() % 7 == 0 ? config.getIntegerList("BadRewards") : config.getIntegerList("SignRewards");
                break;
            case SERIES:
                rewards = config.getIntegerList("SeriesRewards");
                break;
            case PERFECT:
                rewards = config.getIntegerList("PerfectRewards");
                break;
            case TEN_MINUTE:
                rewards = config.getIntegerList("TenMinutesRewards");
                break;
            case TWO_HOURS:
                rewards = config.getIntegerList("TwoHoursRewards");
                break;
            case SIX_HOURS:
                rewards = config.getIntegerList("SixHoursRewards");
                break;
            case QUIZ:
                rewards = config.getIntegerList("QuizRewards");
                break;
            default:
        }
        if(rewards == null || rewards.size() == 0){
            player.sendMessage(Language.TITLE + "暂无奖励");
            return;
        }
        // 随机获取奖品
        int index = random.nextInt(rewards.size());
        int id = rewards.get(index);
        Reward reward = ActivityManager.getReward(id);
        // 判断是指令还是物品
        if(reward.getCommand() != null && !"".equals(reward.getCommand())){
            String command = reward.getCommand().replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }else {
            player.getInventory().addItem(SerializeUtil.deserialize(reward.getStack(), reward.getMeta()));
        }
        // 奖励声音
        if (rewardSound != null) player.playSound(player.getLocation(), rewardSound, 1F, 1F);
    }

}
