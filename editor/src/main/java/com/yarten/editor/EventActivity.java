package com.yarten.editor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.doublecursorseekbar.DoubleSeekBar;
import com.yarten.device.UCP.Controller;
import com.yarten.device.UCP.Controller.Pair;
import com.yarten.circlerefresh.CircleRefreshLayout;
import com.yarten.circlerefresh.RefreshListView;
import com.yarten.device.UCP.Host;
import com.yarten.device.UCP.Manager;
import com.yarten.device.UCP.Signal;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.singleseekbar.SingleSeekBar;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTransition(this, Gravity.RIGHT, Gravity.RIGHT);
        setContentView(R.layout.activity_event);

        initView();
        initSignals();
        initListener();
        initControlList();
    }

    private void initView()
    {
        TextView textView = findViewById(R.id.back_button);
        textView.setText("BACK >>");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    private DrawerAdapter signalAdapter;
    private RefreshListView refreshListView;
    private void initSignals()
    {
        refreshListView = findViewById(R.id.circle_refresh);
        final CommonRecyclerView commonRecyclerView = refreshListView.getCommonRecyclerView();

        signalAdapter = new DrawerAdapter(this, commonRecyclerView, (ViewGroup) findViewById(R.id.temp_layout), new DrawerAdapter.Listener() {
            @Override
            public void onCreateView(WidgetManager.Widget widget)
            {
                commonRecyclerView.setScrollEnable(true);
                if(isInsideControlListView(widget.style.x))
                {
                    Signal signal = new Signal(widget.name, toSignalType(widget.type), widget.description);
                    Controller.Value value = new Controller.Value(-1, 0, 1);
                    Pair pair = new Pair(signal, value);
                    controlAdapter.add(pair);
                }
            }

            @Override
            public void onDragBegin() {
                commonRecyclerView.setScrollEnable(false);
            }
        });

        refreshListView.setOnCircleRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
                updateSignalList();
            }

            @Override
            public void refreshing() {
                Manager.instance.resetSignalList().requireList();
            }
        }).setAdapter(signalAdapter);

        refreshListView.getCommonRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                signalAdapter.hideBubble();
            }
        });

        updateSignalList();
    }

    private void initListener()
    {
        Manager.instance.setControlListListener(new Manager.ControlListListener() {
            @Override
            public void onList(List<Signal> signals) {
                refreshListView.finishRefreshing();
            }
        }).setDiscoverListener(null).setConnectListener(new Manager.ConnectListener() {
            @Override
            public void onConnected(Host host) {
                Utils.makeToast(EventActivity.this, "[" + host.name + "] 已连接");
            }

            @Override
            public void onDisconnected(Host host, boolean isTimeout) {
                Utils.makeToast(EventActivity.this, "[" + host.name + "] 已断开");
            }
        });
    }

    // TODO: 将该过程放在多线程
    private void updateSignalList()
    {
        List<Signal> signals = Manager.instance.getSignals();
        List<WidgetManager.Widget> widgets = new ArrayList<>();
        for(int i = 0, size = signals.size(); i < size; i++)
        {
            Signal signal = signals.get(i);
            WidgetManager.Widget widget = new WidgetManager.Widget();
            widget.type = toWidgetType(signal.getType());
            widget.name = signal.getName();
            widget.description = signal.getDescription();
            widget.style = WidgetManager.Widget.BUTTON_STYLE.clone();
            widget.style.color = Styleable.Color.colors[i % Styleable.Color.colors.length];
            widget.style.text = widget.name;
            widgets.add(widget);
        }

        signalAdapter.updateAll(widgets);
    }

    private CommonRecyclerView.Adapter controlAdapter;
    private CommonRecyclerView commonRecyclerView;
    private static Signal.Type controllerType;

    private void initControlList()
    {
        WidgetManager.Widget widget = WidgetManager.getCurrentWidget();
        controllerType = toSignalType(widget.type);

        Controller controller = WidgetManager.getCurrentController();
        List<Pair> list = controller.getControlList();
        controlAdapter = new CommonRecyclerView.Adapter(this, R.layout.control_item, list, ControlListViewHolder.class);

        commonRecyclerView = findViewById(R.id.control_list);
        commonRecyclerView.setAdapter(controlAdapter);
    }

    private boolean isInsideControlListView(float x)
    {
        final float left = commonRecyclerView.getX();
        final float right = commonRecyclerView.getWidth() + left;
        return x > left && x <right;
    }

    private Signal.Type toSignalType(WidgetManager.Widget.Type type)
    {
        switch (type)
        {
            case Button:
                return Signal.Type.Boolean;
            default:
                return Signal.Type.Undefined;
        }
    }

    private WidgetManager.Widget.Type toWidgetType(Signal.Type type)
    {
        switch (type)
        {
            case Boolean:
                return WidgetManager.Widget.Type.Button;
            case Vector:
                return WidgetManager.Widget.Type.Vector;
            default:
                return WidgetManager.Widget.Type.Undefined;
        }
    }
    
    public static class ControlListViewHolder extends CommonRecyclerView.ViewHolder<Pair>
    {
        private ShapeButton shapeButton;
        private DoubleSeekBar doubleSeekbar;
        private SingleSeekBar singleSeekBar;
        private TextView description;

        private static TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        private static TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                 -1.0f);
        static
        {
            mShowAction.setDuration(500);
            mHiddenAction.setDuration(500);
        }

        public ControlListViewHolder(Context context, View view)
        {
            super(context, view);
            shapeButton = findViewById(R.id.signal);
            singleSeekBar = findViewById(R.id.single_seekbar);
            doubleSeekbar = findViewById(R.id.double_seekbar);
            description = findViewById(R.id.text_description);

            description.setVisibility(View.GONE);

            shapeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(description.getVisibility() == View.GONE)
                    {
                        description.startAnimation(mShowAction);
                        description.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        description.startAnimation(mHiddenAction);
                        description.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public void onBind(final Pair data, int position) {
            switch (controllerType)
            {
                case Vector:
                    switch (data.signal.getType())
                    {
                        case Vector:
                        {
                            doubleSeekbar.setVisibility(View.GONE);
                            singleSeekBar.setVisibility(View.VISIBLE);

                        }break;
                        case Boolean:
                        {
                            doubleSeekbar.setVisibility(View.VISIBLE);
                            singleSeekBar.setVisibility(View.GONE);
                            doubleSeekbar.setRange(-1, 1);
                        }break;
                    }break;
                case Boolean:
                    switch (data.signal.getType())
                    {
                        case Vector:
                        {
                            doubleSeekbar.setVisibility(View.VISIBLE);
                            singleSeekBar.setVisibility(View.GONE);
                            doubleSeekbar.setRange(-1, 1);
                        }break;
                        case Boolean:
                        {
                            doubleSeekbar.setVisibility(View.GONE);
                            singleSeekBar.setVisibility(View.GONE);
                        }break;
                    }break;
            }

            shapeButton.setText(data.signal.getName());
            description.setText(data.signal.getDescription());

            singleSeekBar.setOnValueChangedListener(new SingleSeekBar.OnValueChangedListener() {
                @Override
                public void onChange(float value) {

                }
            });

            doubleSeekbar.setOnValueChangeListener(new DoubleSeekBar.OnValueChangedListener() {
                @Override
                public void onChange(float min, float max) {
                    data.value.min = min;
                    data.value.max = max;
                }
            });
        }

        @Override
        public void onClick(Pair data, int position) {

        }

        @Override
        public boolean onLongClick(Pair data, int position) {
            remove(position);
            return false;
        }
    }
}
