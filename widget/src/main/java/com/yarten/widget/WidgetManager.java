package com.yarten.widget;

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

import com.yarten.rocker.Rocker;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yfic on 2018/1/2.
 */

public class WidgetManager
{
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
            case Rocker:
                view = new Rocker(context);
                length = Widget.ROCKER_LENGTH;
                break;
        }

        length = Utils.dip2px(context, length);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(length, length);
        view.setLayoutParams(params);
        view.setX(widget.style.x);
        view.setY(widget.style.y);

        tryStylize(view, widget.style);
        installControllers(view, widget.type);

        return view;
    }

    public static List<Controller> createControllers(Widget.Type type)
    {
        List<Controller> controllers = new ArrayList<>();
        switch (type)
        {
            case Button:
                controllers.add(new Controller(Controllable.Type.Boolean, "Event"));
                break;
            case Rocker:
                controllers.add(new Controller(Controllable.Type.Vector, "Rocker X"));
                controllers.add(new Controller(Controllable.Type.Vector, "Rocker Y"));
                break;
        }

        return controllers;
    }

    /**
     * 尝试风格化，将可能为Styleable的控件设置Style
     * @param view 由WidgetManager生成的控件
     * @param style 风格
     * @return
     */
    public static boolean tryStylize(View view, Style style)
    {
        if(view instanceof Styleable)
        {
            style.stylize((Styleable)view);
            return true;
        }
        else return false;
    }

    /**
     * 为Controllable的控件安装遥控器
     * @param view 由WidgetManager生成的控件
     * @param type 控件的类型（按钮、遥感等）
     */
    public static void installControllers(View view, Widget.Type type)
    {
        Controllable controllable = (Controllable)view;
        controllable.setControllers(createControllers(type));
    }

    /**
     * 为编辑中的控件绑定一个点击可以弹出对话框的事件，该对话框给出几个选项：
     * 1. Style：进入风格定制界面；不是所有的控件都有该选项；
     * 2. 其他：各个维度的事件定制。
     * @param mainEdit Editor的主Activity
     * @param styleEdit Editor的样式编辑Activity
     * @param eventEdit Editor的事件编辑Activity
     * @param viewGroup
     * @param view
     */
    public static void showDialog(final AppCompatActivity mainEdit, final Class styleEdit, final Class eventEdit, final ViewGroup viewGroup, final View view)
    {
        final int postion = views.indexOf(view);
        final Widget widget = widgets.get(postion);
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

        //region 创建对话框
        new AlertDialog.Builder(mainEdit)
                .setTitle("请选择一个设置项")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        WidgetManager.widget = widget;

                        switch (which)
                        {
                            case 0:
                                if (view instanceof Styleable)
                                {
                                    WidgetManager.styleable = (Styleable)view;
                                    intent = new Intent(mainEdit, styleEdit);
                                    break;
                                }
                                which++;
                            default:
                            {
                                WidgetManager.controller = controllers.get(which-1);
                                intent = new Intent(mainEdit, eventEdit);
                                break;
                            }
                        }

                        mainEdit.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mainEdit).toBundle());
                    }
                })
                .setPositiveButton("取消", null)
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mainEdit, "[" + widget.name + "] 已经被删除", Toast.LENGTH_SHORT).show();
                        remove(viewGroup, view);
                    }
                }).show();
        //endregion
    }

    private static LinkedList<View> views;
    private static LinkedList<Widget> widgets;
    private static Controller controller;
    private static Styleable styleable;
    private static Widget widget;

    public static void init()
    {
        widgets = new LinkedList<>();
        views = new LinkedList<>();
    }

    public static void add(View view, Widget widget)
    {
        widgets.add(widget);
        views.add(view);
    }

    public static void remove(ViewGroup viewGroup, View view)
    {
        int position = views.indexOf(view);
        viewGroup.removeView(view);
        views.remove(position);
        widgets.remove(position);
    }

    public static void removeAll(ViewGroup viewGroup)
    {
        viewGroup.removeAllViews();
        views.clear();
        widgets.clear();
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
