package com.fireflyest.activity;

import com.fireflyest.activity.bean.*;
import com.fireflyest.activity.command.*;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.data.Storage;
import com.fireflyest.activity.listener.PlayerEventListener;
import com.fireflyest.activity.sql.SqlData;
import com.fireflyest.activity.sql.SqlStorage;
import com.fireflyest.activity.sqll.SqLiteData;
import com.fireflyest.activity.sqll.SqLiteStorage;
import com.fireflyest.activity.util.ChatUtils;
import com.fireflyest.activity.util.YamlUtils;
import com.fireflyest.activity.view.MainView;
import com.fireflyest.activity.view.RewardView;
import com.fireflyest.activity.view.TaskView;
import com.fireflyest.gui.api.ViewGuide;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

/**
 * @author Fireflyest
 * 2022/2/24 23:48
 */

public class Activity extends JavaPlugin {

    public static JavaPlugin getInstance() { return plugin; }

    private static JavaPlugin plugin;

    public static final String MAIN_VIEW = "activity.main";
    public static final String REWARD_VIEW = "activity.reward";
    public static final String PLAYTIME_VIEW = "activity.playtime";
    public static final String TASK_VIEW = "activity.task";

    private static Storage storage;
    private static Data data;
    private static Economy economy;
    private static ViewGuide guide;

    public static Data getData() {
        return data;
    }
    public static Storage getStorage() {
        return storage;
    }
    public static Economy getEconomy() {
        return economy;
    }
    public static ViewGuide getGuide() {
        return guide;
    }

    @Override
    public void onEnable() {
        plugin = this;

        this.setupData();
        this.setupEconomy();
        this.setupGuide();

        // 注册事件
        this.getServer().getPluginManager().registerEvents( new PlayerEventListener(), this);

        // 注册指令
        PluginCommand activityCommand = this.getCommand("activity");
        if(activityCommand!=null){
            activityCommand.setExecutor(new ActivityCommand());
            activityCommand.setTabCompleter(new ActivityTab());
        }
        PluginCommand rewardCommand = this.getCommand("reward");
        if(rewardCommand!=null){
            rewardCommand.setExecutor(new RewardCommand());
            rewardCommand.setTabCompleter(new RewardTab());
        }
        PluginCommand taskCommand = this.getCommand("task");
        if(taskCommand!=null){
            taskCommand.setExecutor(new TaskCommand());
            taskCommand.setTabCompleter(new TaskTab());
        }
        PluginCommand signCommand = this.getCommand("sign");
        if(signCommand!=null){
            signCommand.setExecutor(new SignCommand());
            signCommand.setTabCompleter(new SignTab());
        }
        PluginCommand playtimeCommand = this.getCommand("playtime");
        if(playtimeCommand!=null){
            playtimeCommand.setExecutor(new PlaytimeCommand());
        }
        PluginCommand quizCommand = this.getCommand("quiz");
        if(quizCommand!=null){
            quizCommand.setExecutor(new QuizCommand());
            quizCommand.setTabCompleter(new QuizTab());
        }

        ActivityManager.initActivityManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActivityManager.playerJoin(player.getName());
        }

        // 在线提醒
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    String playerName = onlinePlayer.getName();
                    if(ActivityManager.hasTenMinuteReward(playerName)
                            || ActivityManager.hasTwoHourReward(playerName)
                            || ActivityManager.hasSixHourReward(playerName)){
                        onlinePlayer.sendMessage(Language.PLAYTIME_REMIND);
                        ChatUtils.sendCommandButton(onlinePlayer, "在线奖励", "点击打开签到界面", "/activity");
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 20, 20*60*10);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActivityManager.playerQuit(player.getName());
        }
        try {
            storage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupData(){
        YamlUtils.iniYamlUtils(plugin);

        if(Config.SQL){
            if(Config.DEBUG) Bukkit.getLogger().info("使用数据库存储");
            // 数据库访问对象
            try {
                storage = new SqlStorage(Config.URL, Config.USER, Config.PASSWORD);
                data = new SqlData(storage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(Config.DEBUG) Bukkit.getLogger().info("使用本地存储");
            // 本地数据库访问对象
            String url = "jdbc:sqlite:" + getDataFolder() + "/storage.db";

            try {
                storage = new SqLiteStorage(url);
                data = new SqLiteData(storage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        data.createTable(Day.class);
        data.createTable(Festival.class);
        data.createTable(Reward.class);
        data.createTable(User.class);
        data.createTable(Task.class);

    }

    /**
     * 经济插件
     */
    private void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getLogger().warning("Economy not found!");
            return;
        }
        economy = rsp.getProvider();
    }

    /**
     * 界面初始化
     */
    private void setupGuide() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Gui") == null) {
            return;
        }
        RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServer().getServicesManager().getRegistration(ViewGuide.class);
        if (rsp == null) {
            Bukkit.getLogger().warning("Gui not found!");
            return;
        }
        guide = rsp.getProvider();

        guide.addView(MAIN_VIEW, new MainView(Language.PLUGIN_NAME));
        guide.addView(REWARD_VIEW, new RewardView(Language.PLUGIN_NAME));
        guide.addView(TASK_VIEW, new TaskView(Language.PLUGIN_NAME));

    }
}
