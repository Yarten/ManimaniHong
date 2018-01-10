package com.yarten.mainmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarten.shapebutton.ButtonPanel;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReposActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;
    private static ReposActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        instance = this;

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getBottomButton(), buttonPanel, this, MainActivity.class, Gravity.TOP, Gravity.BOTTOM, R.mipmap.up);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_local, R.id.bt1, new ViewPagerHelper.InitHandler()
                {
                    @Override
                    public void onInit(View view)
                    {
                        CommonRecyclerView commonRecyclerView = view.findViewById(R.id.common_list_view);
                        List<String> ls = getList();
                        commonRecyclerView.setAdapter(new CommonRecyclerView.Adapter(ReposActivity.this, R.layout.item_controller, ls, Work.class));
                    }
                }, new ViewPagerHelper.OnTriggleListener()
                {
                    @Override
                    public boolean onTriggle() {
                        return true;
                    }
                })
                .initLayout(R.layout.page_cloud, R.id.bt2, new ViewPagerHelper.InitHandler()
                {
                    @Override
                    public void onInit(View view)
                    {

                    }
                }, new ViewPagerHelper.OnTriggleListener()
                {
                    @Override
                    public boolean onTriggle()
                    {
                        Utils.makeToast(ReposActivity.this, "您还没登录呢");
                        return false;
                    }
                })
                .build(R.id.view_pager);
    }

    private List<String> getList()
    {
        List<String> ls = new ArrayList<>();
        ls.add("Example");
        return ls;
    }

    public static class Work extends CommonRecyclerView.ViewHolder<String>
    {
        private TextView title;

        public Work(Context context, View view)
        {
            super(context, view);

            title = findViewById(R.id.title);
        }

        @Override
        public void onBind(String data, int position) {
            title.setText(data);
        }

        @Override
        public void onClick(String data, int position) {
            Intent intent = new Intent(ReposActivity.instance, com.yarten.manimanihong.MainActivity.class);
            ReposActivity.instance.startActivity(intent);
        }

        @Override
        public boolean onLongClick(String data, final int position) {
            Utils.makeDialog(ReposActivity.instance, "是否删除该遥控器？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Work.super.remove(position);
                }
            });
            return true;
        }
    }
}
