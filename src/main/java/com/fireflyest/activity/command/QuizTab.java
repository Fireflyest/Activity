package com.fireflyest.activity.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QuizTab implements TabCompleter {

    public QuizTab(){
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("quiz")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                tab.add("[题目]");
            }else if(args.length == 2){
                tab.add("[答案]");
            }else if(args.length >= 3){
                tab.add("[选项文本]");
            }
            return tab;
        }
        return null;
    }

}
