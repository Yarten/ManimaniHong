package com.yarten.mainmenu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.yarten.shapebutton.ButtonPanel;

public class DeviceActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getTopButton(), buttonPanel, this, MainActivity.class, Gravity.BOTTOM, Gravity.TOP);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_wifi, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .initLayout(R.layout.page_bluetooth, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .build(R.id.view_pager);
    }
}
