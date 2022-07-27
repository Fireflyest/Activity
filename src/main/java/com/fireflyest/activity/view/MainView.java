package com.fireflyest.activity.view;

import com.fireflyest.activity.util.TimeUtils;
import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class MainView implements View<MainPage> {

    private final Map<String, MainPage> pageMap = new HashMap<>();

    private final String title;

    public MainView(String title) {
        this.title = title;
    }

    @Override
    public MainPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new MainPage(title, target, TimeUtils.getYear(), TimeUtils.getMonth()));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
