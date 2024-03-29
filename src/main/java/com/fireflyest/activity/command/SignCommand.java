package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ConvertUtils;
import com.fireflyest.activity.util.TimeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.api.ViewGuide;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class SignCommand implements CommandExecutor {

    private final Data data;
    private final ViewGuide guide;

    public SignCommand() {
        this.data = Activity.getData();
        this.guide = Activity.getGuide();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("sign")) return true;

        if (args.length == 1) {
            this.executeCommand(sender, args[0]);
        } else {
            this.executeCommand(sender);
        }
        return true;
    }

    private void executeCommand(CommandSender sender){
        this.executeCommand(sender, String.valueOf(TimeUtils.getDay()));
    }

    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        String playerName = player.getName();
        new BukkitRunnable(){
            @Override
            public void run() {

                int dayWantSign = ConvertUtils.parseInt(var1);
                int today = TimeUtils.getDay();

                // 未到
                if(dayWantSign > today){
                    sender.sendMessage(Language.SIGN_IN_FUTURE);
                    // 刷新页面
                    guide.refreshPage(playerName);
                    return;
                }

                // 获取所签日期的数据
                Day day = ActivityManager.getDay(playerName, TimeUtils.getMonth(), dayWantSign);
                // 判断是否已签
                if(day.isSign()){
                    sender.sendMessage(Language.SIGN_IN_ALREADY);
                    // 刷新页面
                    guide.refreshPage(playerName);
                    return;
                }

                // 玩家数据
                User user = ActivityManager.getUser(playerName);
                if(dayWantSign == today){ // 是否当天
                    // 判断昨天是否签到
                    Date lastSignAddOne = DateUtils.addDays(new Date(user.getLastSign()), 1);
                    if (DateUtils.isSameDay(lastSignAddOne, TimeUtils.getDate())){
                        user.setSeries(user.getSeries() + 1);
                        // 每连续签到7天有奖励
                        if(user.getSeries()%7 == 0) RewardManager.giveReward(player, RewardManager.SERIES);
                    }else {
                        user.setSeries(1);
                    }
                    // 设置最后签到时间
                    user.setLastSign(TimeUtils.getTime());

                    // 判断是否全勤
                    if(dayWantSign == TimeUtils.getMaxDay() && user.getSeries() >= TimeUtils.getMaxDay()){
                        RewardManager.giveReward(player, RewardManager.PERFECT);
                    }
                }else {  // 补签
                    if(user.getChance() <= 0){
                        sender.sendMessage(Language.NOT_ENOUGH_CHANCE);
                        return;
                    }
                    user.setChance(user.getChance() - 1);
                }
                // 天数据
                day.setSign(true);
                data.update(day);

                // 签到总数
                user.setSigned(user.getSigned() + 1);
                user.setActivity(user.getActivity() + 1);
                data.update(user);
                // 签到成功
                sender.sendMessage(Language.SIGN_IN_SUCCESS);

                // 奖励
                RewardManager.giveReward(player, RewardManager.SIGN);

                // 刷新页面
                guide.refreshPage(playerName);
            }
        }.runTask(Activity.getInstance());
    }

}
