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
    public static ItemStack MONTH;
    public static ItemStack JAN_DATA;
    public static ItemStack FEB_DATA;
    public static ItemStack MAR_DATA;
    public static ItemStack APR_DATA;
    public static ItemStack MAY_DATA;
    public static ItemStack JUN_DATA;
    public static ItemStack JUL_DATA;
    public static ItemStack AUG_DATA;
    public static ItemStack SEP_DATA;
    public static ItemStack OCT_DATA;
    public static ItemStack NOV_DATA;
    public static ItemStack DEC_DATA;

    public static ItemStack SIGN_REWARDS;
    public static ItemStack BAD_REWARDS;
    public static ItemStack SERIES_REWARDS;
    public static ItemStack PERFECT_REWARDS;
    public static ItemStack TEN_MINUTES_REWARDS;
    public static ItemStack ONE_HOURS_REWARDS;
    public static ItemStack THREE_HOURS_REWARDS;
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
                .command("addReward")
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
                .name("§e§l个人信息")
                .command("activity mine")
                .lore("")
                .lore("§f点击查看")
                .build();
        TASKS = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§e§l活动列表")
                .command("activity task")
                .lore("§f点击查看最新活动")
                .build();
        PLAYTIME = new ViewItemBuilder(XMaterial.CLOCK.parseMaterial())
                .name("§e§l在线数据")
                .lore("")
                .lore("")
                .lore("")
                .lore("§f点击查看奖励")
                .command("activity playtime")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.ENCHANTING_TABLE.parseMaterial())
                .name("§e§l签到数据")
                .lore("")
                .lore("")
                .lore("")
                .lore("§f点击查看奖励")
                .command("activity sign")
                .build();
        MONTH = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§e§l切换月份")
                .lore("§f左右键点击")
                .command("month")
                .build();
        JAN_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l一月数据")
                .lore("§f暂无数据")
                .build();
        FEB_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l二月数据")
                .lore("§f暂无数据")
                .build();
        MAR_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l三月数据")
                .lore("§f暂无数据")
                .build();
        APR_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l四月数据")
                .lore("§f暂无数据")
                .build();
        MAY_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l五月数据")
                .lore("§f暂无数据")
                .build();
        JUN_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l六月数据")
                .lore("§f暂无数据")
                .build();
        JUL_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l七月数据")
                .lore("§f暂无数据")
                .build();
        AUG_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l八月数据")
                .lore("§f暂无数据")
                .build();
        SEP_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l九月数据")
                .lore("§f暂无数据")
                .build();
        OCT_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l十月数据")
                .lore("§f暂无数据")
                .build();
        NOV_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l十一月数据")
                .lore("§f暂无数据")
                .build();
        DEC_DATA = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§e§l十二月数据")
                .lore("§f暂无数据")
                .build();
        BAD_REWARDS  = new ViewItemBuilder(XMaterial.POISONOUS_POTATO.parseMaterial())
                .name("§e§l倒霉奖励")
                .build();
        SIGN_REWARDS = new ViewItemBuilder(XMaterial.BAKED_POTATO.parseMaterial())
                .name("§e§l签到奖励")
                .build();
        SERIES_REWARDS = new ViewItemBuilder(XMaterial.GOLDEN_APPLE.parseMaterial())
                .name("§e§l连续签到奖励")
                .build();
        PERFECT_REWARDS = new ViewItemBuilder(XMaterial.ENCHANTED_GOLDEN_APPLE.parseMaterial())
                .name("§e§l整月签到奖励")
                .build();
        TEN_MINUTES_REWARDS = new ViewItemBuilder(XMaterial.BRICK.parseMaterial())
                .name("§e§l在线十分钟奖励")
                .build();
        ONE_HOURS_REWARDS = new ViewItemBuilder(XMaterial.IRON_INGOT.parseMaterial())
                .name("§e§l在线一小时奖励")
                .build();
        THREE_HOURS_REWARDS = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§l在线三小时奖励")
                .build();
        SIX_HOURS_REWARDS = new ViewItemBuilder(XMaterial.NETHERITE_INGOT.parseMaterial())
                .name("§e§l在线六小时奖励")
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

    public static ItemStack getMonthButton(int month){
        switch (month){
            case 1:
                return JAN_DATA;
            case 2:
                return FEB_DATA;
            case 3:
                return MAR_DATA;
            case 4:
                return APR_DATA;
            case 5:
                return MAY_DATA;
            case 6:
                return JUN_DATA;
            case 7:
                return JUL_DATA;
            case 8:
                return AUG_DATA;
            case 9:
                return SEP_DATA;
            case 10:
                return OCT_DATA;
            case 11:
                return NOV_DATA;
            case 12:
                return DEC_DATA;
            default:
                return BLANK;
        }
    }

}
