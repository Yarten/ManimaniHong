package com.yarten.editor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.xiaoshq.database.DataOperation;
import com.yarten.jsonconverter.Converter;
import com.yarten.ucp.Host;
import com.yarten.ucp.Manager;
import com.yarten.ucp.Signal;
import com.yarten.utils.Interface.EditDialogCallback;
import com.yarten.utils.Style;
import com.yarten.widget.Widget;
import com.example.drawer.DrawerActivity;
import com.yarten.sgbutton.SGFloat;
import com.yarten.sgbutton.SGWidget;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Utils;
import com.yarten.widget.WidgetManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerActivity {

    //region 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_desktop, R.layout.tool_drawer);

        WidgetManager.init();

        initView();
        initMenu();
        initListener();
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
                Utils.makeEditDialog(MainActivity.this, "请填写保存的名字",
                        "保存", "另存为",
                        new EditDialogCallback()
                        {
                            @Override
                            public boolean onConfirm(String text) {
                                if(text.isEmpty())
                                {
                                    Utils.makeToast(MainActivity.this, "名字不能为空");
                                    return false;
                                }
                                saveToDB(text, false);
                                return true;
                            }
                        },
                        new EditDialogCallback()
                        {
                            @Override
                            public boolean onConfirm(final String text)
                            {
                                if(text.isEmpty())
                                {
                                    Utils.makeToast(MainActivity.this, "名字不能为空");
                                    return false;
                                }

                                DataOperation db = DataOperation.instance;
                                if(db.isSolutionExist(text))
                                {
                                    Utils.makeDialog(MainActivity.this, "是否要覆盖原有的文件？", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            saveToDB(text, false);
                                        }
                                    });
                                }
                                else saveToDB(text, true);

                                return true;
                            }
                        });
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
        List<Widget> widgets = initWidget();


        adapter = new DrawerAdapter(this, (ViewGroup) findViewById(R.id.temp_layout), widgets, new DrawerAdapter.Listener()
        {
            @Override
            public void onCreateView(Widget widget)
            {
                View view = WidgetManager.createView(MainActivity.this, widget);
                viewGroup.addView(view);

                WidgetManager.add(view, widget);
                Log.e("Create", "" + view);
                adapter.cleanUp();

                SGFloat sgFloat = new SGFloat(view);
                sgFloat.setOnClickListener(new SGWidget.OnActionListener()
                {
                    @Override
                    public void onAction(View view, MotionEvent event)
                    {
                        WidgetManager.showDialog(MainActivity.this, EditActivity.class, EventActivity.class, viewGroup, view);
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

            @Override
            public void onItemDown() {
                commonRecyclerView.setScrollEnable(false);
            }

            @Override
            public void onItemUp() {
                commonRecyclerView.setScrollEnable(true);
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

    private List<Widget> initWidget()
    {
        List<Widget> widgets = new ArrayList<>();
        Widget.Type[] types = {Widget.Type.Button, Widget.Type.Rocker, Widget.Type.TouchPad, Widget.Type.Orientation, Widget.Type.Accelerometer};
        String[] names = {"Button", "Rocker", "TouchPad", "", ""};
        String[] descriptions = {
                "一个普通按钮，按下为1，松开为0。",
                "一个遥感控件，有两个维度的变换，从-1到1变换。",
                "一个触摸板，根据手指在上面的移动速度和方向作为相应输出",
                "一个方向传感器，可以通过点击来开启或关闭它",
                "一个加速度传感器，可以通过点击来开启或关闭它"
        };
        Style[] styles = {Widget.BUTTON_STYLE, Widget.ROCKER_STYLE, Widget.TOUCHPAD_STYLE, Widget.ORIENTATION_STYLE, Widget.ACCELEROMETER_STYLE};

        for(int i = 0; i < types.length; i++)
        {
            Widget widget = new Widget();
            widget.type = types[i];
            widget.name = names[i];
            widget.description = descriptions[i];
            widget.style = styles[i].clone();
            widget.style.text = widget.name;
            widgets.add(widget);
        }

        return widgets;
    }

    private void initListener()
    {
        Manager.instance.setDiscoverListener(null)
                .setControlListListener(new Manager.ControlListListener() {
                    @Override
                    public void onList(List<Signal> signals) {
                        Log.e("Editor-Main", signals.size() + "");
                    }
                })
                .setConnectListener(new Manager.ConnectListener() {
                    @Override
                    public void onConnected(Host host) {
                        Utils.makeToast(MainActivity.this, "[" + host.name + "] 已连接");
                    }

                    @Override
                    public void onDisconnected(Host host, boolean isTimeout) {
                        Utils.makeToast(MainActivity.this, "[" + host.name + "] 已断开");
                    }
                })
                .resetSignalList()
                .requireList();
    }
    //endregion

    private String name = "";

    private void saveToDB(String name, boolean isNew)
    {
        List<Widget> widgets = WidgetManager.getWidgets();
        List<View> views = WidgetManager.getViews();
        String solution = Converter.toJSON(views, widgets);

        if(this.name.isEmpty())
            isNew = true;

        DataOperation db = DataOperation.instance;
        int id = isNew ? 0 : db.getSolution(this.name).solId;

        if(id == 0)
            db.addSolution(name, solution);
        else db.updateSolution(id, name, solution);

        this.name = name;
    }
}
