package com.fireflyest.activity.view;


import org.fireflyest.craftgui.api.View;

public class RewardView implements View<RewardPage> {

    public static final String NORMAL = "奖品列表";

    private final RewardPage page;

    public RewardView(String title) {
        page = new RewardPage(title, NORMAL, 1);
    }

    @Override
    public RewardPage getFirstPage(String target){
        return page;
    }

    @Override
    public void removePage(String target) {

    }
}
