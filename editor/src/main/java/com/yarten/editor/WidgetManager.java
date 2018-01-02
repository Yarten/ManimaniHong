package com.yarten.editor;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yarten.device.UCP.Controllable;
import com.yarten.device.UCP.Controller;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.utils.Utils;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yfic on 2018/1/2.
 */

public class WidgetManager
{
    public static class Widget
    {
        public enum Type
        {
            Button
        }

        public static final int WIDGET_LENGTH = 100;
        public static final int BUTTON_LENGTH = 100;
        public static Style BUTTON_STYLE;

        public Type type;
        public String name;
        public String description;
        public Style style;

        static
        {
            BUTTON_STYLE = new Style();
            BUTTON_STYLE.color = Styleable.Color.Blue;
            BUTTON_STYLE.shape = Styleable.Shape.Circle;
        }

        public Widget clone()
        {
            Widget widget = new Widget();
            widget.type = type;
            widget.name = name;
            widget.description = description;
            widget.style = style.clone();
            return widget;
        }
    }

    public static View createView(Context context, Widget widget)
    {
        View view = null;
        int length = Widget.WIDGET_LENGTH;
        switch (widget.type)
        {
            case Button:
                view = new ShapeButton(context);
                length = Widget.BUTTON_LENGTH;
                break;
        }

        length = Utils.dip2px(context, length);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(length, length);
        view.setLayoutParams(params);
        view.setX(widget.style.x);
        view.setY(widget.style.y);

        tryStylize(view, widget.style);

        return view;
    }

    public static boolean tryStylize(View view, Style style)
    {
        if(view instanceof Styleable)
        {
            style.stylize((Styleable)view);
            return true;
        }
        else return false;
    }

    public static void showDialog(final Context context, final ViewGroup viewGroup, final int position)
    {
        final WidgetButton widget = widgets.get(position);
        final View view = widget.view;
        final Controllable controllable = (Controllable)view;
        final List<Controller> controllers = controllable.getControllers();

        String[] items;
        int i = 0, size = controllers.size();

        if(view instanceof Styleable)
        {
            items = new String[size+1];
            items[i++] = "Style";
        }
        else items  = new String[size];

        for(int j = 0; j < size; i++, j++)
            items[i] = controllers.get(j).getName();

        new AlertDialog.Builder(context)
                .setTitle("请选择一个设置项")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which)
                        {
                            case 0:
                                if (view instanceof Styleable)
                                {
                                    WidgetManager.styleable = (Styleable)view;
                                    WidgetManager.widget = widget.info;
                                    intent = new Intent(context, EditActivity.class);
                                    break;
                                }
                                which++;
                            default:
                            {
                                WidgetManager.controller = controllers.get(which-1);
                                // TODO: 跳转到控制制定
                                intent = new Intent(context, EditActivity.class);
                                break;
                            }
                        }

                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity)context).toBundle());
                    }
                })
                .setPositiveButton("取消", null)
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Widget info = widgets.get(position).info;
                        Toast.makeText(context, "[" + info.name + "] 已经被删除", Toast.LENGTH_SHORT).show();
                        remove(viewGroup, position);
                    }
                }).show();
    }

    private static class WidgetButton
    {
        WidgetButton(View view, Widget info)
        {
            this.info = info;
            this.view = view;
        }
        Widget info;
        View view;
    }

    private static LinkedList<WidgetButton> widgets;
    private static Controller controller;
    private static Styleable styleable;
    private static Widget widget;

    public static void init()
    {
        widgets = new LinkedList<>();
    }

    public static int add(View view, Widget info)
    {
        widgets.add(new WidgetButton(view, info));
        return widgets.size()-1;
    }

    public static void remove(ViewGroup viewGroup, int position)
    {
        viewGroup.removeView(widgets.get(position).view);
        widgets.remove(position);
    }

    public static Controller getCurrentController()
    {
        return controller;
    }

    public static Styleable getCurrentStyleable()
    {
        return styleable;
    }

    public static Widget getCurrentWidget(){return widget;}

    public static View createCurrentView(Context context)
    {
        return createView(context, widget);
    }

    public static void updateStyle(Styleable styleable)
    {
        widget.style = styleable.getStyle();
        widget.style.stylize(WidgetManager.styleable);
    }
}
