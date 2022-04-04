package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.core.RewardManager;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ConvertUtils;
import com.fireflyest.activity.util.SerializeUtil;
import com.fireflyest.gui.api.ViewGuide;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class RewardCommand implements CommandExecutor {

    private final Data data;
    private final ViewGuide guide;

    public RewardCommand() {
        this.guide = Activity.getGuide();
        this.data = Activity.getData();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("reward")) return true;

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
            case "set":
                sender.sendMessage(Language.TITLE + "未输入奖励类型与奖品id");
                break;
            case "add":
                sender.sendMessage(Language.TITLE+"未输入奖品名称");
                break;
            case "remove":
                sender.sendMessage(Language.TITLE+"未输入奖品ID");
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
            case "set":
                sender.sendMessage(Language.TITLE + "未输入奖品id");
                break;
            case "add":
                executeCommand(sender, var1, var2, null);
                break;
            case "remove":
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        data.delete(Reward.class, "name", var2);
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
        if (var3 != null) {
            var3 = var3.replace("_" , " ");
        }
        String finalVar3 = var3;
        new BukkitRunnable(){
            @Override
            public void run() {

                switch (var1){
                    case "set":
                        int id = ConvertUtils.parseInt(finalVar3);
                        RewardManager.addReward(var2, id);
                        sender.sendMessage(Language.TITLE+"奖品§3ID§7: §3" + id + "§f添加到§3" + var2);
                        break;
                    case "add":
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item.getType() == Material.AIR) {
                            item = new ItemStack(Material.CHEST);
                        }
                        Reward reward = new Reward(var2, finalVar3);
                        reward.setStack(SerializeUtil.serializeItemStack(item));
                        reward.setMeta(SerializeUtil.serializeItemMeta(item));
                        long insertId = data.insert(reward);
                        sender.sendMessage(Language.TITLE+"奖品为§3ID§7: §3" + insertId + "§f，输入§3/reward set [类型] [ID]§f将奖品设置为某类型奖励");
                        break;
                    case "remove":
                        Reward removeReward = data.queryOne(Reward.class, "id", finalVar3);
                        sender.sendMessage(Language.TITLE+"删除奖品§7：§3" + removeReward.getName() + "，§fID§7：§3" + finalVar3);
                        data.delete(removeReward);
                        break;
                    default:
                        sender.sendMessage(Language.TITLE+"指令参数错误");
                }

            }
        }.runTaskAsynchronously(Activity.getInstance());
    }

}
