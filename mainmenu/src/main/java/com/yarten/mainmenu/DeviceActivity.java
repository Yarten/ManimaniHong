package com.yarten.mainmenu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

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
                    TextView wifi_deviceName;
                    Switch wifi_switch;
                    ProgressBar wifi_progressBar;

                    @Override
                    public void onInit(View view) {
                        wifi_deviceName = view.findViewById(R.id.wifi_device_name);
                        wifi_switch = view.findViewById(R.id.wifi_switch);
                        wifi_progressBar = view.findViewById(R.id.wifi_progressBar);
                    }
                })
                .initLayout(R.layout.page_bluetooth, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    TextView bluetooth_deviceName;
                    Switch bluetooth_switch;
                    ProgressBar bluetooth_progressBar;

                    @Override
                    public void onInit(View view) {
                        bluetooth_deviceName = view.findViewById(R.id.bluetooth_device_name);
                        bluetooth_switch = view.findViewById(R.id.bluetooth_switch);
                        bluetooth_progressBar = view.findViewById(R.id.bluetooth_progressBar);
                    }
                })
                .build(R.id.view_pager);
    }
}
