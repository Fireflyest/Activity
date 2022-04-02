package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.gui.api.ViewGuide;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlaytimeCommand implements CommandExecutor {

    private final ViewGuide guide;
    private final Data data;

    public PlaytimeCommand() {
        guide = Activity.getGuide();
        data = Activity.getData();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("playtime")) return true;

        this.executeCommand(sender);
        return true;
    }

    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        String playerName = player.getName();
        new BukkitRunnable(){
            @Override
            public void run() {
                User user = ActivityManager.getUser(playerName);

                if(ActivityManager.hasTenMinuteReward(playerName)){
                    ActivityManager.ridTenMinuteReward(playerName);
                    RewardManager.giveReward(player, RewardManager.TEN_MINUTE);

                    user.setActivity(user.getActivity() + 1);
                }else if(ActivityManager.hasTwoHourReward(playerName)){
                    ActivityManager.ridTwoHourReward(playerName);
                    RewardManager.giveReward(player, RewardManager.TWO_HOURS);

                    user.setActivity(user.getActivity() + 1);
                }else if(ActivityManager.hasSixHourReward(playerName)){
                    ActivityManager.ridSixHourReward(playerName);
                    RewardManager.giveReward(player, RewardManager.SIX_HOURS);

                    user.setActivity(user.getActivity() + 2);
                }else {
                    sender.sendMessage(Language.NOT_ENOUGH_PLAYTIME);
                }
                data.update(user);
                guide.refreshPage(playerName);
            }
        }.runTaskAsynchronously(Activity.getInstance());
    }

}
