package com.fireflyest.activity.listener;

import com.cryptomorin.xseries.XSound;
import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ChatUtils;
import com.fireflyest.activity.util.ItemUtils;
import com.fireflyest.activity.util.TimeUtils;
import com.fireflyest.gui.api.ViewGuide;
import com.fireflyest.gui.event.ViewClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEventListener implements Listener {

    private final Sound clickSound;
    private final Sound pageSound;

    private final ViewGuide guide;
    private final Data data;

    public PlayerEventListener(){

        this.clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_OFF.parseSound();
        this.pageSound = XSound.ITEM_BOOK_PAGE_TURN.parseSound();

        this.guide = Activity.getGuide();
        this.data = Activity.getData();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        new BukkitRunnable(){
            @Override
            public void run() {
                User user = ActivityManager.getUser(playerName);
                // 新用户新建数据存储
                if (user == null) {
                    user = new User(playerName, player.getUniqueId().toString(),  TimeUtils.getDate());
                    ActivityManager.addUser(user);
                }
                // 过月份给予补签机会
                if(user.getMonth() != TimeUtils.getMonth()){
                    user.setChance(user.getChance() + Config.DEFAULT_CHANCE);
                    user.setMonth(TimeUtils.getMonth());
                    data.update(user);
                }
                // 未签到发送签到提醒
                Day day = ActivityManager.getDay(playerName);
                if(!day.isSign()){
                    player.sendMessage(Language.SIGN_IN_REMIND);
                    ChatUtils.sendCommandButton(player, "每日签到", "点击打开签到界面", "/activity");
                }
                // 开始计算在线时间
                ActivityManager.playerJoin(playerName);
            }
        }.runTaskAsynchronously(Activity.getInstance());
    }

    @EventHandler
    public void onViewClick(ViewClickEvent event){
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;
        // 获取点击的物品
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        // 点击的玩家
        Player player = null;
        if(event.getWhoClicked() instanceof Player) player = (Player)event.getWhoClicked();
        if (player == null) return;
        // 获取点击数据
        String value = ItemUtils.getItemValue(item);
        if("".equals(value))return;

        // 翻页
        if (value.equals("month")){
            player.playSound(player.getLocation(), pageSound, 1F, 1F);
            if (event.isLeftClick()){
                guide.prePage(player);
            }else if (event.isRightClick()){
                guide.nextPage(player);
            }
            return;
        }
        // 执行指令
        if(value.startsWith("/")) player.performCommand(value.substring(1));
        player.playSound(player.getLocation(), clickSound, 1F, 1F);

    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        if(!event.getPlayer().hasPermission("market.create"))return;
        if("activity".equalsIgnoreCase(event.getLine(0))){
            event.setLine(0, Language.PLUGIN_NAME);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        ActivityManager.playerQuit(event.getPlayer().getName());
    }

}
