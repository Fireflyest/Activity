package com.fireflyest.activity.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.activity.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ActivityItem {

    public static ItemStack AIR;
    public static ItemStack CLOSE;
    public static ItemStack RETURN;
    public static ItemStack BLANK;
    public static ItemStack PAGE;

    public static ItemStack TODAY;
    public static ItemStack SIGNED;
    public static ItemStack NOT_SIGN;
    public static ItemStack QING_MING;
    public static ItemStack FESTIVAL;

    public static ItemStack ACTIVITY;
    public static ItemStack ACTIVITY_SKULL;
    public static ItemStack SIGN;
    public static ItemStack PLAYTIME;
    public static ItemStack TASKS;
    public static ItemStack TASK;

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

    private ActivityItem(){
    }

    static {

        AIR = new ItemStack(Material.AIR);

        CLOSE = ItemUtils.createItem(XMaterial.REDSTONE.parseMaterial(), "§c§l关闭", "/activity close", "§f点击关闭");
        RETURN = ItemUtils.createItem(XMaterial.REDSTONE.parseMaterial(), "§e§l返回", "/activity return", "§f点击返回");

        BLANK = ItemUtils.createItem(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), " ", null);

        TODAY = ItemUtils.createItem(XMaterial.FEATHER.parseMaterial(), null, null);

        SIGNED = ItemUtils.createItem(XMaterial.FILLED_MAP.parseMaterial(), null, null);

        NOT_SIGN = ItemUtils.createItem(XMaterial.PAPER.parseMaterial(), null, null);

        QING_MING = ItemUtils.createItem(XMaterial.END_ROD.parseMaterial(), null, null);

        FESTIVAL = ItemUtils.createItem(XMaterial.MAP.parseMaterial(), null, null);

        ACTIVITY = ItemUtils.createItem(XMaterial.BEACON.parseMaterial(), "§e§l活跃奖励", "/activity rewards");
        ACTIVITY_SKULL = ItemUtils.createItem(XMaterial.PLAYER_HEAD.parseMaterial(), "§e§l活跃奖励", "/activity rewards");

        TASKS = ItemUtils.createItem(XMaterial.BOOKSHELF.parseMaterial(), "§e§l活动列表", "/activity task");

        PLAYTIME = ItemUtils.createItem(XMaterial.CLOCK.parseMaterial(), "§e§l在线数据", "/playtime");

        SIGN = ItemUtils.createItem(XMaterial.WRITABLE_BOOK.parseMaterial(), "§e§l签到数据", "month");

        TASK = ItemUtils.createItem(XMaterial.BOOK.parseMaterial(), "§e§l任务书", null);


        BAD_REWARDS = ItemUtils.createItem(XMaterial.TRAPPED_CHEST.parseMaterial(), "§e§l倒霉奖励", null, "§f运气不好的时候会得到");
        SIGN_REWARDS = ItemUtils.createItem(XMaterial.CHEST.parseMaterial(), "§e§l签到奖励", null, "§f每次签到都能获得，节日双倍");
        SERIES_REWARDS = ItemUtils.createItem(XMaterial.ENDER_CHEST.parseMaterial(), "§e§l连续签到奖励", null, "§f每连续签到七天会得到");
        PERFECT_REWARDS = ItemUtils.createItem(XMaterial.SMITHING_TABLE.parseMaterial(), "§e§l整月签到奖励", null, "§f每连续签到整个月会得到");
    }

    public static ItemStack getPageItem(int i){
        ItemStack tmp = PAGE.clone();
        tmp.setAmount(i);
        ItemUtils.addLore(tmp, "§7<§e"+(i)+"§7>§0");
        ItemUtils.setItemValue(tmp, String.format("page %d", i) );
        return tmp;
    }

    public static ItemStack getNotSignItem(XCalendar.XDay xDay){
        if(xDay == null) return NOT_SIGN.clone();
        boolean f = false;
        if(xDay.getSolarTerms() != null){
            f = true;
            if("清明".equals(xDay.getSolarTerms())) return QING_MING.clone();
        }
        Material material = null;
        if(xDay.getSolarFestival() != null){
            f = true;
            material = festivalItemMap.get(xDay.getSolarFestival());
        }
        if(xDay.getLunarFestival() != null){
            f = true;
            material = festivalItemMap.get(xDay.getLunarFestival());
        }
        if (material == null) {
            return f ? FESTIVAL.clone() : NOT_SIGN.clone();
        }
        ItemStack item = new ItemStack(material);
        ItemUtils.addItemHide(item);
        return item;
    }

}
