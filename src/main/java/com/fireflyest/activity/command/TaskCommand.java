package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Task;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ConvertUtils;
import com.fireflyest.activity.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

public class TaskCommand implements CommandExecutor {

    private final Data data;
    private final ViewGuide guide;

    public TaskCommand() {
        this.guide = Activity.getGuide();
        this.data = Activity.getData();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("task")) return true;

        if(!sender.hasPermission("activity.task")){
            sender.sendMessage(Language.TITLE + String.format("你没有权限§3%s§f来使用该指令", "activity.task"));
            return true;
        }

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
            guide.openView(player, Activity.REWARD_VIEW, sender.getName());
        }else {
            sender.sendMessage(Language.PLAYER_COMMAND);
        }
    }

    private void executeCommand(CommandSender sender, String var1){
        switch (var1){
            case "add":
                sender.sendMessage(Language.TITLE+"未输入活动名称");
                break;
            case "desc":
            case "command":
            case "remove":
                sender.sendMessage(Language.TITLE+"未输入活动ID");
                break;
            default:
                sender.sendMessage(Language.TITLE+"指令参数错误");
        }
    }

    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1){
            case "add":
                executeCommand(sender, var1, var2, null);
                break;
            case "remove":
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Task removeTask = data.queryOne(Task.class, "id", var2);
                        sender.sendMessage(Language.TITLE+"删除活动§7：§3" + removeTask.getName());
                        data.delete(removeTask);
                    }
                }.runTaskAsynchronously(Activity.getInstance());
                break;
            default:
                sender.sendMessage(Language.TITLE+"指令参数错误");
        }
    }

    private void executeCommand(CommandSender sender, String var1, String var2, String var3){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                switch (var1){
                    case "add":
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item.getType() == Material.AIR) {
                            item = new ItemStack(Material.BOOK);
                        }
                        long deadline = var3 == null ? 0 : ((long) ConvertUtils.parseInt(var3) *1000*60*60) + TimeUtils.getTime();
                        Task task = new Task(var2, deadline);
                        task.setStack(SerializeUtil.serializeItemStack(item));
                        task.setMeta(SerializeUtil.serializeItemMeta(item));
                        long insertId = data.insert(task);
                        sender.sendMessage(Language.TITLE+"成功新建活动§3ID§7：§3" + insertId + "§f，输入§3/task desc [ID] [简介] §f来设置展示的简介");
                        break;
                    case "desc":
                        Task descTask = data.queryOne(Task.class, "id", var2);
                        descTask.setDesc(var3);
                        data.update(descTask);
                        sender.sendMessage(Language.TITLE+ String.format("活动§3%s§f的简介成功修改", descTask.getName()));
                        break;
                    case "command":
                        Task commandTask = data.queryOne(Task.class, "id", var2);
                        commandTask.setCommand(var3.replace("_", " "));
                        data.update(commandTask);
                        sender.sendMessage(Language.TITLE+ String.format("活动§3%s§f的指令成功修改", commandTask.getName()));
                        break;
                    default:
                        sender.sendMessage(Language.TITLE+"指令参数错误");
                }
            }
        }.runTaskAsynchronously(Activity.getInstance());
    }

}
