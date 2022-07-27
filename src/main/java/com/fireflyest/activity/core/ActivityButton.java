package com.fireflyest.activity.core;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.item.ViewItemBuilder;

import java.util.HashMap;
import java.util.Map;

public class ActivityButton {

    public static ItemStack AIR  = new ItemStack(Material.AIR);
    public static ItemStack CLOSE;
    public static ItemStack BACK;
    public static ItemStack BLANK;
    public static ItemStack PAGE_PRE;
    public static ItemStack PAGE_NEXT;
    public static ItemStack PAGE_PRE_DISABLE;
    public static ItemStack PAGE_NEXT_DISABLE;
    public static ItemStack ADD_REWARD;

    public static ItemStack TODAY;
    public static ItemStack SIGNED;
    public static ItemStack MISS_SIGN;
    public static ItemStack FUTURE_SIGN;
    public static ItemStack QING_MING;
    public static ItemStack FESTIVAL;

    public static ItemStack ACTIVITY;
    public static ItemStack SIGN;
    public static ItemStack PLAYTIME;
    public static ItemStack TASKS;

    public static ItemStack SIGN_REWARDS;
    public static ItemStack BAD_REWARDS;
    public static ItemStack SERIES_REWARDS;
    public static ItemStack PERFECT_REWARDS;
    public static ItemStack TEN_MINUTES_REWARDS;
    public static ItemStack TWO_HOURS_REWARDS;
    public static ItemStack SIX_HOURS_REWARDS;

    private static final Map<String, Material> festivalItemMap = new HashMap<String, Material>(){
        {
            put("*元旦", XMaterial.BLAZE_SPAWN_EGG.parseMaterial());
            put("情人节", XMaterial.ROSE_BUSH.parseMaterial());
            put("植树节", XMaterial.OAK_SAPLING.parseMaterial());
            put("愚人节", XMaterial.PUFFERFISH.parseMaterial());
            put("*劳动节", XMaterial.IRON_PICKAXE.parseMaterial());
            put("青年节", XMaterial.BOOK.parseMaterial());
            put("儿童节", XMaterial.LEATHER_HORSE_ARMOR.parseMaterial());
            put("教师节", XMaterial.WRITABLE_BOOK.parseMaterial());
            put("*国庆节", XMaterial.NETHER_STAR.parseMaterial());
            put("万圣节", XMaterial.JACK_O_LANTERN.parseMaterial());
            put("光棍节", XMaterial.STICK.parseMaterial());
            put("平安夜", XMaterial.APPLE.parseMaterial());
            put("圣诞节", XMaterial.BLAZE_POWDER.parseMaterial());
            put("*春节", XMaterial.FIREWORK_ROCKET.parseMaterial());
            put("元宵节", XMaterial.LANTERN.parseMaterial());
            put("*端午节", XMaterial.DRAGON_HEAD.parseMaterial());
            put("*中秋节", XMaterial.PUMPKIN_PIE.parseMaterial());
            put("*除夕", XMaterial.RED_BANNER.parseMaterial());
        }
    };

    private ActivityButton(){
    }

    static {
        CLOSE = new ViewItemBuilder(XMaterial.REDSTONE.parseMaterial())
                .name("§c§l关闭")
                .command("activity close")
                .lore("§f点击关闭")
                .build();
        BACK = new ViewItemBuilder(XMaterial.REDSTONE.parseMaterial())
                .name("§c§l返回")
                .command("activity")
                .lore("§f点击返回")
                .build();
        BLANK = new ViewItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial())
                .name(" ")
                .build();
        PAGE_NEXT = new ViewItemBuilder(XMaterial.LIME_DYE.parseMaterial())
                .name("§a§l▶")
                .command("page next")
                .build();
        PAGE_PRE = new ViewItemBuilder(XMaterial.LIME_DYE.parseMaterial())
                .name("§a§l◀")
                .command("page pre")
                .build();
        PAGE_NEXT_DISABLE = new ViewItemBuilder(XMaterial.GRAY_DYE.parseMaterial())
                .name("§7§l▷")
                .build();
        PAGE_PRE_DISABLE = new ViewItemBuilder(XMaterial.GRAY_DYE.parseMaterial())
                .name("§7§l◁")
                .build();
        ADD_REWARD = new ViewItemBuilder(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial())
                .name("§3§l添加")
                .command("reward")
                .build();
        TODAY = new ViewItemBuilder(XMaterial.WRITABLE_BOOK.parseMaterial())
                .name("§f§l[§a§l签到§f§l]")
                .build();
        SIGNED = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§f§l[§6§l已签§f§l]")
                .build();
        MISS_SIGN = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§f§l[§c§l漏签§f§l]")
                .build();
        FUTURE_SIGN = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§f§l[§2§l待签§f§l]")
                .build();
        QING_MING = new ViewItemBuilder(XMaterial.OXEYE_DAISY.parseMaterial())
                .name("§f§l[§2§l待签§f§l]")
                .build();
        FESTIVAL = new ViewItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial())
                .build();
        ACTIVITY = new ViewItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .name("§e§l活跃奖励")
                .command("activity rewards")
                .lore("")
                .lore("§f点击查看礼包")
                .build();
        TASKS = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§e§l活动列表")
                .command("activity task")
                .lore("§f点击查看最新活动")
                .build();
        PLAYTIME = new ViewItemBuilder(XMaterial.CLOCK.parseMaterial())
                .name("§e§l在线数据")
                .command("activity playtime")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.ENCHANTING_TABLE.parseMaterial())
                .name("§e§l签到数据")
                .command("activity sign")
                .build();
        BAD_REWARDS  = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§e§l倒霉奖励")
                .build();
        SIGN_REWARDS = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§e§l签到奖励")
                .build();
        SERIES_REWARDS = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§e§l连续签到奖励")
                .build();
        PERFECT_REWARDS = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§e§l整月签到奖励")
                .build();
    }

    public static ItemStack getNotSignItem(ActivityCalendar.Day xDay, boolean miss){
        ItemStack defaultNotSign = miss ? MISS_SIGN.clone() : FUTURE_SIGN.clone();
        if(xDay == null) return defaultNotSign;
        boolean festival = false;
        Material material = null;
        if(xDay.getSolarTerms() != null) if("清明".equals(xDay.getSolarTerms())) return QING_MING.clone();
        if(xDay.getSolarFestival() != null){
            festival = true;
            material = festivalItemMap.get(xDay.getSolarFestival());
        }
        if(xDay.getLunarFestival() != null){
            festival = true;
            material = festivalItemMap.get(xDay.getLunarFestival());
        }
        // 非有按钮的节日
        if (!festival) return defaultNotSign;
        // 给按钮设置材料
        if (material == null) material = FESTIVAL.getType();
        return new ViewItemBuilder(material)
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE)
                .name(miss ? "§f§l[§c§l漏签§f§l]" : "§f§l[§2§l待签§f§l]")
                .build();
    }

}
