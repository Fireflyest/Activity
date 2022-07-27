package com.fireflyest.activity.listener;

import com.cryptomorin.xseries.XSound;
import com.fireflyest.activity.Activity;
import com.fireflyest.activity.bean.Day;
import com.fireflyest.activity.bean.Reward;
import com.fireflyest.activity.bean.User;
import com.fireflyest.activity.core.ActivityManager;
import com.fireflyest.activity.data.Config;
import com.fireflyest.activity.data.Data;
import com.fireflyest.activity.data.Language;
import com.fireflyest.activity.util.ChatUtils;
import com.fireflyest.activity.util.TimeUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.event.ViewClickEvent;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.fireflyest.craftgui.util.TranslateUtils;

public class PlayerEventListener implements Listener {

    private final Activity activity;
    private final Sound clickSound;
    private final Sound pageSound;
    private final Sound removeSound;

    private final ViewGuide guide;
    private final Data data;

    public PlayerEventListener(){
        this.activity = Activity.getInstance();

        this.clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_OFF.parseSound();
        this.pageSound = XSound.ITEM_BOOK_PAGE_TURN.parseSound();
        this.removeSound = XSound.BLOCK_ANVIL_PLACE.parseSound();

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
        }.runTaskAsynchronously(activity);
    }

    @EventHandler
    public void onViewClick(ViewClickEvent event){
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;
        // 获取点击的物品
        ItemStack item = event.getCurrentItem();
        // 点击的玩家
        Player player = (Player)event.getWhoClicked();
        // 获取点击数据
        String value = ItemUtils.getItemValue(item);
        if("".equals(value))return;

        // 翻页
        if (value.contains("page")){
            // 不用刷新
            event.setRefresh(false);

            if (Config.DEBUG) activity.getLogger().info("action -> " + value);
            player.playSound(player.getLocation(), pageSound, 1F, 1F);
            if (value.contains("pre")){
                guide.prePage(player);
            }else if (value.contains("next")){
                guide.nextPage(player);
            }
            return;
        }

        if (value.startsWith("reward ")){
            if (!event.isShiftClick()) return;
            if (!player.hasPermission("activity.admin")) return;
            String command = "reward remove " + value.split(" ")[1];
            if (Config.DEBUG) activity.getLogger().info(String.format("remove -> %s", command));
            player.performCommand(command);
            player.playSound(player.getLocation(), removeSound, 1F, 1F);
            return;
        }

        // 执行指令
        event.setRefresh(false);
        if (Config.DEBUG) activity.getLogger().info(String.format("command -> %s", value));
        player.performCommand(value);
        player.playSound(player.getLocation(), clickSound, 1F, 1F);

    }

    @EventHandler
    public void onViewPlace(ViewPlaceEvent event) {
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;

        ItemStack clickItem = event.getCurrentItem();
        if (clickItem == null) return;

        Player player = ((Player) event.getWhoClicked());

        ItemStack placeItem = event.getCursorItem();

        // 获取点击数据
        String value = ItemUtils.getItemValue(clickItem);
        if("".equals(value))return;

        if ("reward".equals(value)) {
            if (!player.hasPermission("activity.admin")) return;
            String name = TranslateUtils.translate(placeItem.getType()) + (placeItem.getAmount() > 1 ? "×"+placeItem.getAmount() : "");
            Reward reward = new Reward(name, "");
            reward.setStack(SerializeUtil.serializeItemStack(placeItem));
            reward.setMeta(SerializeUtil.serializeItemMeta(placeItem));
            data.insert(reward);
        }

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
