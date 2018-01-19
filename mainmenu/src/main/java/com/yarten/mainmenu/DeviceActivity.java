package com.yarten.mainmenu;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.airbubble.BubbleToast;
import com.yarten.ucp.Host;
import com.yarten.ucp.Manager;
import com.yarten.shapebutton.ButtonPanel;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Interface.EditDialogCallback;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;
    private static DeviceActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        instance = this;

        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getTopButton(), buttonPanel, this, MainActivity.class, Gravity.BOTTOM, Gravity.TOP, R.mipmap.down);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_wifi, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    private CommonRecyclerView.Adapter adapter;

                    @Override
                    public void onInit(View view)
                    {
                        CommonRecyclerView commonRecyclerView = view.findViewById(R.id.common_list_view);
                    //    List<Host> hosts = copy(Manager.instance.getConnectedHosts());
                        List<Host> hosts = Manager.instance.getAllHosts();
                        adapter = new CommonRecyclerView.Adapter(DeviceActivity.this, R.layout.item_wifi, hosts, Device.class);
                        commonRecyclerView.setAdapter(adapter);
                        initListener(adapter);
                    }
                }, new ViewPagerHelper.OnTriggleListener() {
                    @Override
                    public boolean onTriggle() {
                        return true;
                    }
                })
                .initLayout(R.layout.page_bluetooth, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    @Override
                    public void onInit(View view) {

                    }
                }, new ViewPagerHelper.OnTriggleListener() {
                    @Override
                    public boolean onTriggle() {
                        WindowManager windowManager = (WindowManager)DeviceActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        DisplayMetrics outMetrics = new DisplayMetrics();
                        windowManager.getDefaultDisplay().getMetrics(outMetrics);
                        int width = outMetrics.widthPixels;
                        int height = outMetrics.heightPixels;
                        BubbleToast bubbleToast = new BubbleToast(DeviceActivity.this,width/2,height/1.4f,"蓝牙功能尚未开发",Gravity.BOTTOM,(float)1.5);
                        bubbleToast.show();
                        Log.i("Width",width + "");
                        Log.i("Height", height + "");
                        return false;
                    }
                })
                .build(R.id.view_pager);
    }

    @Override
    protected void onDestroy()
    {
        Manager.instance.stopListenCast();
        super.onDestroy();
    }

    private void initListener(final CommonRecyclerView.Adapter adapter)
    {
        Manager.instance.setConnectListener(new Manager.ConnectListener() {
            @Override
            public void onConnected(Host host) {
                int position = find(adapter, host);
                if(position >= 0)
                    adapter.notifyUpdate(position);
            }

            @Override
            public void onDisconnected(Host host, boolean isTimeout)
            {
                int position = find(adapter, host);
                if(position >= 0)
                {
                    adapter.notifyUpdate(position);
                }

                Utils.makeToast(DeviceActivity.this, "密码不正确，请重试");
            }
        }).setDiscoverListener(new Manager.DiscoverListener() {
            @Override
            public void onHello(Host host, int position) {
                adapter.notifyAdd(position);
                Log.e("On Hello", host.name + " " + host.host);
             //   adapter.add(host, position);
            }

            @Override
            public void onGoodbye(int position) {
                adapter.notifyRemove(position);
                //adapter.remove(position);
            }
        }).setControlListListener(null).startListenCast();
    }

    private int find(CommonRecyclerView.Adapter adapter, Host host)
    {
        List<Host> hosts = adapter.getItems();
        for(int i = 0, size = hosts.size(); i < size; i++)
            if(hosts.get(i).host.equals(host.host))
                return i;
        return -1;
    }

    private List<Host> copy(List<Host> src)
    {
        List<Host> dst = new ArrayList<>();
        copy(src, dst);
        return dst;
    }

    private void copy(List<Host> src, List<Host> dst)
    {
        dst.clear();
        for(Host i : src)
            dst.add(i.clone());
    }

    public static class Device extends CommonRecyclerView.ViewHolder<Host>
    {
        private TextView name;
        private Switch aSwitch;
        private ProgressBar progressBar;

        public Device(Context context, View view)
        {
            super(context, view);
            name = findViewById(R.id.wifi_device_name);
            aSwitch = findViewById(R.id.wifi_switch);
            progressBar = findViewById(R.id.wifi_progressBar);

            progressBar.setVisibility(View.GONE);
            aSwitch.setChecked(false);
            aSwitch.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBind(final Host data, final int position)
        {
            name.setText(data.name);
            switch (data.getState())
            {
                case Connected:
                    aSwitch.setChecked(true);
                    aSwitch.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    break;
                case Discovered:
                    aSwitch.setChecked(false);
                    aSwitch.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    break;
                case Connecting:
                    aSwitch.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }

            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (data.getState())
                    {
                        case Connected:
                            Manager.instance.disconnect(data);
                            break;
                        case Discovered:
                            aSwitch.setChecked(false);
                            Utils.makeEditDialog(DeviceActivity.instance, "请输入[" + data.name + "]的密码", new EditDialogCallback() {
                                @Override
                                public void onConfirm(String text) {
                                    Manager.instance.connect(data, text);
                                    notifyUpdate(position);
                                }
                            }); break;
                    }
                }
            });
        }

        @Override
        public void onClick(final Host data, int position) {

        }

        @Override
        public boolean onLongClick(Host data, int position) {
            return false;
        }
    }
}
