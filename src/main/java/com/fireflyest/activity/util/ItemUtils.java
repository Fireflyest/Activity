package com.fireflyest.activity.util;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 */
public class ItemUtils {

    private ItemUtils(){
    }

    /**
     * 设置物品名称
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.setDisplayName(name.replace("&", "§"));
            item.setItemMeta(meta);
        }
    }

    public static void setItemValue(ItemStack item, String value) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return;
        meta.setLocalizedName(value);
        item.setItemMeta(meta);
    }

    /**
     * 设置物品注释值
     */
    public static void addItemData(ItemStack item, String key, Object value) {
        addLore(item, "§3§l" + key+"§7:§f "+value);
    }

    public static String getItemValue(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return "";
        return meta.getLocalizedName();
    }

    /**
     * 添加注释
     * @param item 物品
     * @param lore 注释
     */
    public static void addLore(ItemStack item, String lore){
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.add(lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    public static void addItemHide(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
    }

    /**
     * 获取玩家头颅
     * @param player 玩家
     * @return ItemStack
     */
    public static ItemStack createSkull(ItemStack item, OfflinePlayer player){
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createItem(Material material, String name, String command, String... lore){
        ItemStack  item = new ItemStack(Material.STONE);
        if (material != null) {
            item = new ItemStack(material);
            if (name != null) ItemUtils.setDisplayName(item, name);
            if (command != null)  ItemUtils.setItemValue(item, command);
            for (String l : lore) {
                ItemUtils.addLore(item, l);
            }
        }
        return item;
    }

}
