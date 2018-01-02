package com.yarten.editor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.yarten.editor.WidgetManager.Widget;
import com.example.drawer.DrawerActivity;
import com.yarten.sgbutton.SGFloat;
import com.yarten.sgbutton.SGWidget;
import com.yarten.utils.CommonRecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_desktop, R.layout.tool_drawer);

        WidgetManager.init();

        initView();
    }

    private DrawerAdapter adapter;
    private void initView()
    {
        final CommonRecyclerView commonRecyclerView = findViewById(R.id.common_list_view);
        final ViewGroup viewGroup = findViewById(R.id.root_view);
        List<Widget> widgets = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Widget widget = new Widget();
            widget.type = Widget.Type.Button;
            widget.name = "B" + i;
            widget.description = "D" + i;
            widget.style = Widget.BUTTON_STYLE.clone();
            widget.style.text = "C" + i;
            widgets.add(widget);
        }


        adapter = new DrawerAdapter(this, (ViewGroup) findViewById(R.id.temp_layout), widgets, new DrawerAdapter.Listener()
        {
            @Override
            public void onCreateView(Widget widget)
            {
                commonRecyclerView.setScrollEnable(true);
                View view = WidgetManager.createView(MainActivity.this, widget);
                SGFloat sgFloat = new SGFloat(view);
                viewGroup.addView(view);

                final int position = WidgetManager.add(view, widget);

                sgFloat.setOnClickListener(new SGWidget.OnActionListener()
                {
                    @Override
                    public void onAction(View view, MotionEvent event)
                    {
                        WidgetManager.showDialog(MainActivity.this, viewGroup, position);
                    }
                });
            }

            @Override
            public void onDragBegin()
            {
                commonRecyclerView.setScrollEnable(false);
                MainActivity.super.closeDrawer();
            }
        });

        commonRecyclerView.setAdapter(adapter);
    }
}
