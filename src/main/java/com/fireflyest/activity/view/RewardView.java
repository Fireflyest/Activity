package com.fireflyest.activity.view;


import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class RewardView implements View<RewardPage> {

    private final Map<String, RewardPage> pageMap = new HashMap<>();
    public static final String SIGN = "签到奖品";
    public static final String PLAYTIME = "在线奖品";


    public RewardView(String title) {
        pageMap.put(SIGN, new RewardPage(title, SIGN, 1));
        pageMap.put(PLAYTIME, new RewardPage(title, PLAYTIME, 1));
    }

    @Override
    public RewardPage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {

    }
}
