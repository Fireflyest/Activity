package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ConvertUtils;
import com.fireflyest.activity.util.YamlUtils;
import com.fireflyest.activity.view.RewardView;
import com.fireflyest.gui.api.ViewGuide;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActivityCommand implements CommandExecutor {

    private final Data data;
    private final ViewGuide guide;

    public ActivityCommand() {
        this.guide = Activity.getGuide();
        this.data = Activity.getData();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("activity")) return true;

        switch (args.length){
            case 1:
                this.executeCommand(sender, args[0]);
                break;
            case 2:
                this.executeCommand(sender, args[0], args[1]);
                break;
            case 3:
                this.executeCommand(sender, args[0], args[1], args[2]);
                break;
            default:
                this.executeCommand(sender);
                break;
        }
        return true;
    }

    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player != null) {
            guide.openView(player, Activity.MAIN_VIEW, sender.getName());
        }else {
            sender.sendMessage(Language.PLAYER_COMMAND);
        }
    }

    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        String playerName = player.getName();
        switch (var1){
            case "reload":
                if(!sender.isOp())return;
                sender.sendMessage(Language.RELOADING);
                YamlUtils.loadConfig();
                sender.sendMessage(Language.RELOADED);
                break;
            case "rewards":
                guide.openView(player, Activity.REWARD_VIEW, RewardView.NORMAL);
                break;
            case "task":
                guide.openView(player, Activity.TASK_VIEW, playerName);
                break;
            case "playtime":
                break;
            case "close":
                player.closeInventory();
                break;
            case "test":
                if(!sender.isOp()) return;
                RewardManager.giveReward(player, RewardManager.SIGN);
                break;
            case "return":
                guide.openView(player, Activity.MAIN_VIEW, sender.getName());
                break;
            case "rank":
                if(!sender.hasPermission("activity.rank")){
                    sender.sendMessage(Language.TITLE + String.format("你没有权限§3%s§f来使用该指令", "activity.rank"));
                    return;
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        List<User> ranks = data.query(User.class);
                        ranks.sort((u1, u2) -> {
                            if(u1.getActivity() == u2.getActivity()) return 0;
                            return u1.getActivity() > u2.getActivity() ? 1 : -1;
                        });
                        sender.sendMessage(Language.ACTIVITY_RANK);
                        int i = 1;
                        for (User rank : ranks) {
                            sender.sendMessage(String.format("§3%s§7.§f %s      §3%s", i, rank.getName(), rank.getActivity()));
                            i++;
                            if(i > 10) break;
                        }
                    }
                }.runTaskAsynchronously(Activity.getInstance());
                break;
            default:
        }
    }

    private void executeCommand(CommandSender sender, String var1, String var2){
        executeCommand(sender, var1, var2, "1");
    }

    private void executeCommand(CommandSender sender, String var1, String var2, String var3){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        int num = ConvertUtils.parseInt(var3);
        User user = ActivityManager.getUser(var2);
        switch (var1){
            case "chance":
                if(!sender.hasPermission("activity.chance")){
                    sender.sendMessage(Language.TITLE + String.format("你没有权限§3%s§f来使用该指令", "activity.chance"));
                    return;
                }
                user.setChance(user.getChance() + num);
                sender.sendMessage(Language.TITLE + "已为玩家§3" + var2 + "§f添加§3"+ num +"§f补签机会");
                break;
            case "add":
                if(!sender.hasPermission("activity.add")){
                    sender.sendMessage(Language.TITLE + String.format("你没有权限§3%s§f来使用该指令", "activity.chance"));
                    return;
                }
                user.setActivity(user.getActivity() + num);
                sender.sendMessage(Language.TITLE + "已为玩家§3" + var2 + "§f添加§3" + num + "§f活跃值");
                break;
            default:
        }
        data.update(user);
    }

}
