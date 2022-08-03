package com.fireflyest.activity.core;

import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class QuizManager {

    private static Quiz quiz;
    private static BukkitTask bukkitTask;

    private QuizManager(){

    }

    public static boolean createQuiz(String title, char right, String... options){
        // 判断问题结束没
        if(bukkitTask != null && !bukkitTask.isCancelled()){
            return false;
        }

        // 创建问题
        quiz = new Quiz(title + "(  )", right, options);

        bukkitTask = new BukkitRunnable(){
            @Override
            public void run() {
                if(quiz.getWinner() == null){
                    // 无人答对
                    Bukkit.broadcastMessage(Language.TITLE + "此轮抢答没有玩家选择正确");
                }else {
                    String playerName = quiz.getWinner();
                    // 广播
                    Bukkit.broadcastMessage(
                            Language.WIN_PLAYER
                                    .replace("%player%", playerName)
                                    .replace("%activity%", String.valueOf(Config.QUIZ_ACTIVITY)));
                    // 给活跃度
                    User user = ActivityManager.getUser(playerName);
                    user.setActivity(user.getActivity() +Config.QUIZ_ACTIVITY);
                    Activity.getData().update(user);
                    // 抢答奖励
                    RewardManager.giveReward(Bukkit.getPlayer(playerName), RewardManager.QUIZ);
                }

                cancel();
            }
        }.runTaskLater(Activity.getInstance(), 20*60);

        return true;
    }

    public static void publicQuiz(){
        // 发布题目
        Bukkit.broadcastMessage(Language.TITLE + quiz.getTitle());
        // 发布选项
        for(Player player : Bukkit.getOnlinePlayers()){
            char option = 'A';
            for(String optionText : quiz.getOptions()){
                ChatUtils.sendQuizOption(player, option, optionText);
                option++;
            }
        }
    }

    public static void answerQuiz(Player player, char option){
        String playerName = player.getName();

        // 判断是否选过
        if(quiz.isSelected(playerName)){
            player.sendMessage(Language.SELECTED);
            return;
        }else {
            quiz.addSelected(playerName);
        }

        // 发送
        Bukkit.broadcastMessage(
                Language.SELECT_OPTION
                        .replace("%player%", playerName)
                        .replace("%option%", String.valueOf(option)));

        //判断是否正确
        if(option == quiz.getRight()){
            player.sendMessage(Language.RIGHT_OPTION);
        }else {
            player.sendMessage(Language.FALSE_OPTION);
            return;
        }

        // 判断是否被抢答
        if(quiz.isFinish()) {
            player.sendMessage(Language.TITLE + "你不是第一个答对的，不获得奖励");
            return;
        }

        quiz.finish();
        quiz.setWinner(playerName);
    }

    static class Quiz{

        private boolean finish;
        private String winner;
        private final String title;
        private final char right;
        private final List<String> options;
        private final Set<String > users;

        private Quiz(String title, char right, String... options){
            this.finish = false;
            this.title = title;
            this.right = right;
            this.options = new ArrayList<>(Arrays.asList(options));
            this.users = new HashSet<>();
        }

        public List<String> getOptions() {
            return options;
        }

        public String getTitle() {
            return title;
        }

        public char getRight() {
            return right;
        }

        public boolean isFinish() {
            return finish;
        }

        public void finish(){
            finish = true;
        }

        public void setWinner(String winner) {
            this.winner = winner;
        }

        public String getWinner() {
            return winner;
        }

        public boolean isSelected(String playerName){
            return users.contains(playerName);
        }

        public void addSelected(String playerName){
            users.add(playerName);
        }

    }

}

