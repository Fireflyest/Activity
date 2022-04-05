package com.fireflyest.activity.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RewardTab implements TabCompleter {

    private static final List<String> list = new ArrayList<>();

    public RewardTab(){
        list.add("add");
        list.add("set");
        list.add("remove");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("reward")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                for(String sub : list){
                    if(sub.contains(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                if("add".equalsIgnoreCase(args[0])){
                    tab.add("[名称]");
                }else if("set".equalsIgnoreCase(args[0])){
                    tab.add("SignRewards");
                    tab.add("BadRewards");
                    tab.add("SeriesRewards");
                    tab.add("PerfectRewards");
                    tab.add("TenMinutesRewards");
                    tab.add("TwoHoursRewards");
                    tab.add("SixHoursRewards");
                }else if("remove".equalsIgnoreCase(args[0])){
                    tab.add("[ID]");
                }
            }else if(args.length == 3){
                if("set".equalsIgnoreCase(args[0])){
                    tab.add("[id]");
                }else if("add".equalsIgnoreCase(args[0])){
                    tab.add("<指令>");
                }
            }
            return tab;
        }
        return null;
    }

}
