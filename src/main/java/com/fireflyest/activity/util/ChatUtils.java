package com.fireflyest.activity.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Fireflyest
 * OPEN_URL  在用户的浏览器打开指定地址
 * OPEN_FILE  在用户的电脑打开指定文件
 * RUN_COMMAND  让用户运行指令
 * SUGGEST_COMMAND  在用户的输入框设置文字
 * CHANGE_PAGE  改变书本的页码
 *
 * SHOW_TEXT  显示一个文本
 * SHOW_ACHIEVEMENT  显示一个成就及其介绍
 * SHOW_ITEM  显示一个物品的名字和其他信息
 * SHOW_ENTITY  显示一个实体的名字，ID和其他信息
 *
 */
public class ChatUtils {

    private static final TextComponent LEFT = new TextComponent("[");
    private static final TextComponent RIGHT = new TextComponent("]");

    private ChatUtils(){

    }

    /**
     * 发送一个可执行指令的按钮文本
     * @param player 玩家
     * @param display 文本
     * @param hover 指令提示
     * @param command 所执行指令
     */
    @SuppressWarnings("deprecation")
    public static void sendCommandButton(Player player, String display, String hover, String command) {
        player.spigot().sendMessage(new ComponentBuilder(LEFT)
                .append(display)
                .color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                .append(RIGHT)
                .reset()
                .color(ChatColor.WHITE)
                .create()
        );
    }

    @SuppressWarnings("deprecation")
    public static void sendQuizOption(Player player, char option, String text){
        player.spigot().sendMessage(new ComponentBuilder(LEFT)
                .append(String.valueOf(option)).color(ChatColor.DARK_AQUA)
                .append(RIGHT).color(ChatColor.WHITE)
                .append(" ")
                .append(text)
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.format("点击选择%s", option)).create()))
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/quiz "+option))
                .create());
    }

    private static String n2l(int n){
        switch (n){
            case 1:
            default:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
    }

}
