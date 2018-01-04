package com.yarten.editor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.yarten.editor.WidgetManager.Widget;
import com.example.drawer.DrawerActivity;
import com.yarten.sgbutton.SGFloat;
import com.yarten.sgbutton.SGWidget;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_desktop, R.layout.tool_drawer);

        WidgetManager.init();

        initView();
        initMenu();
    }

    private void initMenu()
    {
        FloatingActionsMenu menu = findViewById(R.id.menu_button);
        FloatingActionButton clearAll = findViewById(R.id.clear_all_option);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WidgetManager.removeAll(viewGroup);
            }
        });

        FloatingActionButton save = findViewById(R.id.save_option);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FloatingActionButton quit = findViewById(R.id.quit_option);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.makeDialog(MainActivity.this, "是否放弃修改？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private DrawerAdapter adapter;
    private ViewGroup viewGroup;
    private void initView()
    {
        final CommonRecyclerView commonRecyclerView = findViewById(R.id.common_list_view);
        viewGroup = findViewById(R.id.widget_layout);
        List<Widget> widgets = new ArrayList<>();

        {
            Widget widget = new Widget();
            widget.type = Widget.Type.Button;
            widget.name = "Button";
            widget.description = "一个普通按钮，按下为1，松开为0";
            widget.style = Widget.BUTTON_STYLE.clone();
            widget.style.text = widget.name;
            widgets.add(widget);
        }


        adapter = new DrawerAdapter(this, commonRecyclerView, (ViewGroup) findViewById(R.id.temp_layout), widgets, new DrawerAdapter.Listener()
        {
            @Override
            public void onCreateView(Widget widget)
            {
                View view = WidgetManager.createView(MainActivity.this, widget);
                SGFloat sgFloat = new SGFloat(view);
                viewGroup.addView(view);

                WidgetManager.add(view, widget);
                Log.e("Create", "" + view);

                sgFloat.setOnClickListener(new SGWidget.OnActionListener()
                {
                    @Override
                    public void onAction(View view, MotionEvent event)
                    {
                        WidgetManager.showDialog(MainActivity.this, viewGroup, view);
                    }
                });

                MainActivity.super.lockDrawer(false);
            }

            @Override
            public void onDragBegin()
            {
                MainActivity.super.lockDrawer(true);
                MainActivity.super.closeDrawer();
            }
        });

        commonRecyclerView.setAdapter(adapter);
        commonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                adapter.hideBubble();
            }
        });

        super.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                adapter.hideBubble();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                adapter.hideBubble();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


}
