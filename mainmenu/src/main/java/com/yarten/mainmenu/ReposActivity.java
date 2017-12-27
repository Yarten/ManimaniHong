package com.yarten.mainmenu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.yarten.shapebutton.ButtonPanel;

public class ReposActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getBottomButton(), buttonPanel, this, MainActivity.class, Gravity.TOP, Gravity.BOTTOM);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_local, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .initLayout(R.layout.page_cloud, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .build(R.id.view_pager);
    }
}
