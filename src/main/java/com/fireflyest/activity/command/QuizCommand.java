package com.fireflyest.activity.command;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.core.QuizManager;
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

public class QuizCommand implements CommandExecutor {

    public QuizCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("quiz")) return true;

        switch (args.length){
            case 0: // 帮助
                this.executeCommand(sender);
                break;
            case 1: // 答题
                this.executeCommand(sender, args[0]);
                break;
            default:
                this.executeCommand(sender, args);
                break;
        }
        return true;
    }

    private void executeCommand(CommandSender sender){
        sender.sendMessage(Language.TITLE + "正确用法§3/quiz [问题] [正确选项(A、B、C)] [选项...]");
    }


    private void executeCommand(CommandSender sender, String[] args){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        if(!sender.hasPermission("activity.quiz")){
            sender.sendMessage(Language.TITLE + String.format("你没有权限§3%s§f来使用该指令", "activity.quiz"));
            return;
        }

        String[] options = new String[args.length-2];
        System.arraycopy(args, 2, options, 0, options.length);

        boolean success = QuizManager.createQuiz(args[0], args[1].charAt(0), options);
        if(success){
            QuizManager.publicQuiz();
        }else {
            sender.sendMessage(Language.TITLE + "上一个抢答还未结束！");
        }
    }

    /**
     * 回答问题
     * @param sender 发送者
     * @param var1 选项
     */
    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }

        QuizManager.answerQuiz(player, var1.charAt(0));
    }

}
