package com.yarten.editor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.drawer.DrawerActivity;
import com.yarten.device.UCP.Package;
import com.yarten.sgbutton.SGFloat;
import com.yarten.sgbutton.SGWidget;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.CommonRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_desktop, R.layout.tool_drawer);

        initView();
    }

    private DrawerAdapter adapter;
    private void initView()
    {
        final CommonRecyclerView commonRecyclerView = findViewById(R.id.common_list_view);
        final ViewGroup viewGroup = findViewById(R.id.root_view);
        List<DrawerAdapter.Widget> widgets = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            DrawerAdapter.Widget widget = new DrawerAdapter.Widget();
            widget.type = DrawerAdapter.Widget.Type.Button;
            widget.name = "B" + i;
            widget.description = "D" + i;
            widget.style = DrawerAdapter.Widget.BUTTON_STYLE.clone();
            widget.style.text = "C" + i;
            widgets.add(widget);
        }


        adapter = new DrawerAdapter(this, (ViewGroup) findViewById(R.id.temp_layout), widgets, new DrawerAdapter.Listener() {
            @Override
            public void onCreateView(View view, ViewGroup.LayoutParams params, float x, float y) {
                commonRecyclerView.setScrollEnable(true);
                SGFloat sgFloat = new SGFloat(view);
                viewGroup.addView(view, params);
                view.setX(x);
                view.setY(y);
                sgFloat.setOnClickListener(new SGWidget.OnActionListener() {
                    @Override
                    public void onAction(View view, MotionEvent event) {

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
