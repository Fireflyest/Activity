package com.fireflyest.activity.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QuizTab implements TabCompleter {

    private static final List<String> list = new ArrayList<>();

    public QuizTab(){
        list.add("[题目]");
        list.add("[选项]");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("quiz")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                for(String sub : list){
                    if(sub.contains(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                tab.add("[正确选项]");
                tab.add("A");
                tab.add("B");
                tab.add("C");
                tab.add("D");
                tab.add("...");
            }else if(args.length >= 3){
                tab.add("[选项]");
            }
            return tab;
        }
        return null;
    }

}
