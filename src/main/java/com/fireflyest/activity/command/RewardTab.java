package com.fireflyest.activity.command;

import com.fireflyest.activity.util.YamlUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
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
                    if(sub.startsWith(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                if("add".equalsIgnoreCase(args[0])){
                    tab.add("[name]");
                }else if("set".equalsIgnoreCase(args[0])){
                    ConfigurationSection rewards = YamlUtils.getConfig().getConfigurationSection("Rewards");
                    if (rewards != null) {
                        tab.addAll(rewards.getKeys(false));
                    }
                }else if("remove".equalsIgnoreCase(args[0])){
                    tab.add("[ID]");
                }
            }else if(args.length == 3){
                if("set".equalsIgnoreCase(args[0])){
                    tab.add("[id]");
                }else if("add".equalsIgnoreCase(args[0])){
                    tab.add("<command>");
                }
            }
            return tab;
        }
        return null;
    }

}
