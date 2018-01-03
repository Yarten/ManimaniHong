package com.yarten.mainmenu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.yarten.shapebutton.ButtonPanel;

public class ReposActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getBottomButton(), buttonPanel, this, MainActivity.class, Gravity.TOP, Gravity.BOTTOM, R.mipmap.up);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_local, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    ImageView local_img;
                    ImageButton BT_local_play;
                    ImageButton BT_local_delete;
                    ImageButton BT_local_edit;

                    @Override
                    public void onInit(View view) {
                        local_img = view.findViewById(R.id.local_image);
                        BT_local_play = view.findViewById(R.id.local_play_button);
                        BT_local_edit = view.findViewById(R.id.local_edit_button);
                        BT_local_delete = view.findViewById(R.id.local_delete_button);

                    }
                })
                .initLayout(R.layout.page_cloud, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    ImageView cloud_img;
                    ImageButton BT_cloud_play;
                    ImageButton BT_cloud_edit;
                    ImageButton BT_cloud_delete;

                    @Override
                    public void onInit(View view) {
                        cloud_img = view.findViewById(R.id.cloud_image);
                        BT_cloud_play = view.findViewById(R.id.cloud_play_button);
                        BT_cloud_edit = view.findViewById(R.id.cloud_edit_button);
                        BT_cloud_delete = view.findViewById(R.id.cloud_delete_button);
                    }
                })
                .build(R.id.view_pager);
    }
}
