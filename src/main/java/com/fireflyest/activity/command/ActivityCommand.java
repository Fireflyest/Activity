package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ConvertUtils;
import com.fireflyest.activity.util.TimeUtils;
import com.fireflyest.activity.view.MainView;
import com.fireflyest.activity.view.RewardView;
import com.fireflyest.gui.api.ViewGuide;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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


//        Player player = (sender instanceof Player)? (Player)sender : null;
//        if(args.length == 0) {
//            if(!(sender instanceof Player)) {
//                sender.sendMessage(Language.PLAYER_COMMAND);
//                return true;
//            }
//            GuiManager.openGui(player);
//        }else if(args.length == 1){
//            if(args[0].equalsIgnoreCase("help")) {
//                for(String msg : Language.HELP){ sender.sendMessage(msg.replace("&", "§")); }
//            }else if(args[0].equalsIgnoreCase("reload")){
//                if(!sender.isOp())return true;
//                sender.sendMessage(Language.RELOADING);
//                YamlDriver.loadConfig();
//                GuiManager.iniGuiManager(manager);
//                sender.sendMessage(Language.RELOADED);
//            }else if(args[0].equalsIgnoreCase("default")){
//                if(!sender.isOp())return true;
//                YamlDriver.upDateConfig();
//                sender.sendMessage(Language.RELOADING);
//                YamlDriver.loadConfig();
//                sender.sendMessage(Language.RELOADED);
//            }else if(args[0].equalsIgnoreCase("test")){
//                if(player == null) { sender.sendMessage(Language.PLAYER_COMMAND); return true; }
//            }else if(args[0].equalsIgnoreCase("rank")) {
//                new BukkitRunnable() {
//                    @Override
//                    public void run() {
//                        Map<Double, String> rank = new TreeMap<>();
//                        sender.sendMessage(Language.ACTIVITY_RANK);
//                        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
//                            String name = offlinePlayer.getName();
//                            int day = ActivityTime.getDay();
//                            if(!manager.containsDay(name, day))continue;
//                            double add = ((double)Math.random())/1000;
//                            rank.put(manager.getActivity(name, day)*-1-add, name);
//                        }
//                        int i = 0;
//                        for(Map.Entry<Double, String> entry : rank.entrySet()) {
//                            i++;
//                            sender.sendMessage("[§6"+i+"§r]§3"+entry.getValue()+"§7 ▶ §r"+entry.getKey().intValue()*-1);
//                        }
//                        cancel();
//                    }
//                }.runTask(Activity.getProvidingPlugin(Activity.class));
//            }else if(args[0].equalsIgnoreCase("playtime")){
//                if(player == null) { sender.sendMessage(Language.PLAYER_COMMAND); return true; }
//                player.openInventory(GuiManager.PLAYTIME);
//            }else if(args[0].equalsIgnoreCase("task")){
//                if(player == null) { sender.sendMessage(Language.PLAYER_COMMAND); return true; }
//                GuiManager.openTaskGui(player);
//            }else if(args[0].equalsIgnoreCase("initial")){
//                for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
//                    manager.clearData(p.getName());
//                }
//                sender.sendMessage(Language.TITLE + "已清空玩家签到数据");
//            }
//        }else if(args.length == 2) {
//            if(args[0].equalsIgnoreCase("add") && sender.hasPermission("activity.add")) {
//                manager.addChance(args[1], 1);
//                sender.sendMessage(Language.ADD_CHANCE.replace("%player%", args[1]).replace("%amount%", "1"));
//            }else if(args[0].equalsIgnoreCase("data")){
//                if(player == null) { sender.sendMessage(Language.PLAYER_COMMAND); return true; }
//                player.sendMessage(Language.DAY_ACTIVITY
//                        .replace("%days%", manager.getDayList(args[1]).size()+"")
//                        .replace("%activity%", manager.getTotalActivity(args[1])+"")
//                        .replace("%player%", args[1])
//                );
//                TreeSet<String>days = new TreeSet<>(manager.getDayList(args[1]));
//                for(String day: days){
//                    int activity = manager.getActivity(args[1], Integer.parseInt(day));
//                    ActivityChat.sendDayActivity(
//                            player,
//                            day,
//                            ActivityConvert.convertBar(activity),
//                            "活跃度: "+activity + "  在线: " + ActivityConvert.convertTime(manager.getOnlineTime(args[1], Integer.parseInt(day)))
//                    );
//                }
//            }else if(args[0].equalsIgnoreCase("quiz")){
//                if(player == null) { sender.sendMessage(Language.PLAYER_COMMAND); return true; }
//                QuizManager.selectOption(player, args[1]);
//            }
//        }else if(args.length == 3) {
//            if(args[0].equalsIgnoreCase("add") && sender.hasPermission("activity.add")) {
//                manager.addChance(args[1], Integer.parseInt(args[2]));
//                sender.sendMessage(Language.ADD_CHANCE.replace("%player%", args[1]).replace("%amount%", args[2]));
//            } else if(args[0].equalsIgnoreCase("task") && sender.hasPermission("activity.task")) {
//                manager.addTask(args[1], args[2], 0+"");
//                Player target = Bukkit.getPlayer(args[1]);
//                if(target != null)target.sendMessage(Language.ADD_TASK.replace("%task%", args[2]));
//            }
//        }else {
//            String[] option = new String[args.length-2];
//            System.arraycopy(args, 2, option, 0, option.length);
//            if(args[0].equalsIgnoreCase("quiz") && sender.hasPermission("activity.quiz")) {
//                QuizManager.sendSingleQuiz(args[1], option);
//            }
//        }
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
            case "return":
                guide.openView(player, Activity.MAIN_VIEW, sender.getName());
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
                user.setChance(user.getChance() + num);
                sender.sendMessage(Language.TITLE + "已为玩家§3" + var2 + "§f添加§3"+ num +"§f补签机会");
                break;
            case "add":
                user.setActivity(user.getActivity() + num);
                sender.sendMessage(Language.TITLE + "已为玩家§3" + var2 + "§f添加§3" + num + "§f活跃值");
                break;
            default:
        }
        data.update(user);
    }

}
