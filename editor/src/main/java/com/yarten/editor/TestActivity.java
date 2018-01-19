package com.yarten.editor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yarten.circlerefresh.RefreshListView;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.widget.Widget;
import com.yarten.widget.WidgetManager;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private DrawerAdapter adapter;
    private ViewGroup viewGroup;
    private void initView() {
        final CommonRecyclerView commonRecyclerView = findViewById(R.id.common_list_view);
        viewGroup = findViewById(R.id.widget_layout);
        List<Widget> widgets = initWidget();
        final RefreshListView refreshListView = findViewById(R.id.circle_refresh);

        adapter = new DrawerAdapter(this, (ViewGroup) findViewById(R.id.temp_layout), widgets, new DrawerAdapter.Listener() {
            @Override
            public void onCreateView(Widget widget) {

            }

            @Override
            public void onDragBegin() {
            }

            @Override
            public void onItemDown() {
                commonRecyclerView.setScrollEnable(false);
                refreshListView.setEnable(false);
            }

            @Override
            public void onItemUp() {
                commonRecyclerView.setScrollEnable(true);
                refreshListView.setEnable(true);
            }
        });

        commonRecyclerView.setAdapter(adapter);
        commonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                adapter.hideBubble();
            }
        });

        refreshListView.setAdapter(adapter);
    }

    private List<Widget> initWidget ()
    {
        List<Widget> widgets = new ArrayList<>();

        for(int i = 0;  i < 10; i++)
        {
            Widget widget = new Widget();
            widget.type = Widget.Type.Button;
            widget.name = "Button";
            widget.description = "一个普通按钮，按下为1，松开为0";
            widget.style = Widget.BUTTON_STYLE.clone();
            widget.style.text = widget.name;
            widgets.add(widget);
        }

        return widgets;
    }
}
