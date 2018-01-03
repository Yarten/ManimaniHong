package com.yarten.mainmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

import com.yarten.shapebutton.ButtonPanel;

public class AboutActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getRightButton(), buttonPanel, this, MainActivity.class, Gravity.LEFT, Gravity.RIGHT, R.mipmap.left);
    }
}
