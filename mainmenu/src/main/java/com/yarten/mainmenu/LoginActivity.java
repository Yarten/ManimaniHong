package com.yarten.mainmenu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.yarten.shapebutton.ButtonPanel;

public class LoginActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonPanel = findViewById(R.id.button_panel);

        ActivityHelper.initTriangle(buttonPanel.getLeftButton(), buttonPanel, this, MainActivity.class, Gravity.RIGHT, Gravity.LEFT);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_login, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .initLayout(R.layout.page_register, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                })
                .build(R.id.view_pager);
    }
}
