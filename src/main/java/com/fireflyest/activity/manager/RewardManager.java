package com.fireflyest.activity.manager;

import com.fireflyest.activity.convert.ActivityConvert;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RewardManager {

    private static DataManager manager;

    private RewardManager(){

    }

    public static void iniRewardManager(DataManager dataManager){
        manager = dataManager;
    }

    /**
     * 签到奖励
     * @param player 玩家
     * @param weekend 是否周末
     */
    public static void signInReward(Player player, boolean weekend){
        String name = player.getName();
        int random;
        if(weekend) {
            random = (int)(Config.WEEKEND.size()*Math.random());
            player.sendMessage(Language.SIGN_IN_WEEKEND);
            serverCommand(Config.WEEKEND.get(random).replace("%player%", name));
        }else {
            random = (int)(Config.REWARDS.size()*Math.random());
            serverCommand(Config.REWARDS.get(random).replace("%player%", name));
        }
        player.sendMessage(Language.SIGN_IN_SUCCESS);
        if(random == 0)player.sendMessage(Language.UNLUCK_TODAY);
    }

    /**
     * 活跃奖励
     * @param player 玩家
     * @param reward 奖励
     */
    public static void aReward(Player player, String reward){
        //判断活跃是否足够或是否领取过
        String name = player.getName();
        if(manager.getTotalActivity(name) < Config.getActivityInt(reward+".Price")){
            player.sendMessage(Language.NOT_ENOUGH_ACTIVITY);
            return;
        }
        if(manager.hasAReward(name, reward)){
            player.sendMessage(Language.SIGNIN_REWARD_LIMIT);
            return;
        }
        manager.addAReward(name, reward);
        player.sendMessage(Language.EXCHANGE_SUCCESS);
        player.playSound(player.getLocation(), "entity.player.levelup", 1, 1);
        serverCommand(Config.getActivityString(reward+".Reward").replace("%player%", name));
    }

    /**
     * 在线奖励
     * @param player 玩家
     * @param reward 奖励
     */
    public static void pReward(Player player, String reward){
        String name = player.getName();
        if(manager.hasPReward(name, reward)){
            player.sendMessage(Language.SIGNIN_REWARD_LIMIT);
            return;
        }
        String time = Config.getPlaytimeString(reward+".Time"), type = Config.getPlaytimeString(reward+".Type");
        long date =ActivityConvert.convertTime(time);
        if("本月".equals(type)){
            if(manager.getTotalTime(name)+OnlineManager.getPlaytime(name) < date){
                player.sendMessage(Language.NOT_ENOUGH_PLAYTIME);
                return;
            }
            manager.addPReward(name, reward);
        }else {
            if(OnlineManager.getPlaytime(name) < date){
                player.sendMessage(Language.NOT_ENOUGH_PLAYTIME);
                return;
            }
            OnlineManager.savePlayerTime(name);
        }
        player.sendMessage(Language.EXCHANGE_SUCCESS);
        player.playSound(player.getLocation(), "entity.player.levelup", 1, 1);
        RewardManager.serverCommand(Config.getPlaytimeString(reward+".Reward").replace("%player%", name));
    }

    /**
     * 签到奖励
     * @param player 玩家
     * @param reward 奖励
     */
    public static void sReward(Player player, String reward){
        String name = player.getName();
        int amount = Config.getSigninInt(reward+".Amount");
        String type = Config.getSigninString(reward+".Type");
        if("连续".equals(type)){
            if(manager.getSeries(name) < amount){
                player.sendMessage(Language.NOT_ENOUGH_SIGNIN);
                return;
            }
            if(manager.hasSReward(name, reward)){
                player.sendMessage(Language.SIGNIN_REWARD_LIMIT);
                return;
            }
        }else {
            if(manager.getAddUp(name) < amount){
                player.sendMessage(Language.NOT_ENOUGH_SIGNIN);
                return;
            }
        }
        manager.addSReward(name, reward);
        player.sendMessage(Language.EXCHANGE_SUCCESS);
        player.playSound(player.getLocation(), "entity.player.levelup", 1, 1);
        RewardManager.serverCommand(Config.getSigninString(reward+".Reward").replace("%player%", name));
    }

    /**
     * 后台指令
     * @param command 指令
     */
    public static void serverCommand(String command){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}
