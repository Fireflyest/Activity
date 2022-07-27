package com.fireflyest.activity.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class TaskView implements View<TaskPage> {

    private final Map<String, TaskPage> pageMap = new HashMap<>();

    private final String title;

    public TaskView(String title) {
        this.title = title;
    }

    @Override
    public TaskPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new TaskPage(title, target));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
