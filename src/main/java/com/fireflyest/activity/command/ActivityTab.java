package com.fireflyest.activity.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActivityTab implements TabCompleter {

    private static final List<String> list = new ArrayList<>();

    public ActivityTab(){
        list.add("help");
        list.add("chance");
        list.add("add");
        list.add("rank");
        list.add("task");
        list.add("sign");
        list.add("mine");
        list.add("reload");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("activity")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                for(String sub : list){
                    if(sub.contains(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                if("chance".equalsIgnoreCase(args[0]) || "add".equalsIgnoreCase(args[0])){
                    return null;
                }
            }else if(args.length == 3){
                if("add".equalsIgnoreCase(args[0]) || "chance".equalsIgnoreCase(args[0])){
                    tab.add("<数量>");
                    tab.add("1");
                    tab.add("2");
                }
            }
            return tab;
        }
        return null;
    }

}
