package com.yarten.editor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.doublecursorseekbar.DoubleSeekBar;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.ucp.Controller.Pair;
import com.yarten.ucp.Host;
import com.yarten.ucp.Manager;
import com.yarten.ucp.Signal;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.singleseekbar.SingleSeekBar;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Utils;
import com.yarten.widget.Widget;
import com.yarten.widget.WidgetManager;

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
    private void initSignals()
    {
        final CommonRecyclerView commonRecyclerView = findViewById(R.id.signal_list);

        signalAdapter = new DrawerAdapter(this, (ViewGroup) findViewById(R.id.temp_layout), new DrawerAdapter.Listener() {
            @Override
            public void onCreateView(Widget widget)
            {
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

            }

            @Override
            public void onItemDown() {
                commonRecyclerView.setScrollEnable(false);
            //    signalAdapter.hideBubble();
            }

            @Override
            public void onItemUp() {
                commonRecyclerView.setScrollEnable(true);
            }
        });

        commonRecyclerView.setAdapter(signalAdapter);
        updateSignalList();
    }

    private void initListener()
    {
        Manager.instance.setControlListListener(new Manager.ControlListListener()
        {
            @Override
            public void onList(List<Signal> signals) {
                // TODO
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
        List<Widget> widgets = new ArrayList<>();
        for(int i = 0, size = signals.size(); i < size; i++)
        {
            Signal signal = signals.get(i);
            Widget widget = new Widget();
            widget.type = Widget.Type.Button;
            widget.name = signal.getName();
            widget.description = signal.getDescription();
            widget.style = Widget.BUTTON_STYLE.clone();
            widget.style.color = Styleable.Color.colors[i % Styleable.Color.colors.length];
            widget.style.text = widget.name;
            widgets.add(widget);
        }

        signalAdapter.updateAll(widgets);
    }

    private CommonRecyclerView.Adapter controlAdapter;
    private CommonRecyclerView commonRecyclerView;
    private static Controllable.Type controllerType;

    private void initControlList()
    {
        Widget widget = WidgetManager.getCurrentWidget();
        controllerType = toSignalType(widget.type);

        Controller controller = WidgetManager.getCurrentController();
        List<Pair> list = controller.getControlList();
        controlAdapter = new CommonRecyclerView.Adapter(this, R.layout.item_control, list, ControlListViewHolder.class);

        commonRecyclerView = findViewById(R.id.control_list);
        commonRecyclerView.setAdapter(controlAdapter);
    }

    private boolean isInsideControlListView(float x)
    {
        int[] position = new int[2];
        commonRecyclerView.getLocationOnScreen(position);
        final float left = position[0];
        final float right = commonRecyclerView.getWidth() + left;
        return x > left && x <right;
    }

    private Controllable.Type toSignalType(Widget.Type type)
    {
        switch (type)
        {
            case Button:
                return Controllable.Type.Boolean;
            case Rocker:
                return Controllable.Type.Vector;
            default:
                return Controllable.Type.Undefined;
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
