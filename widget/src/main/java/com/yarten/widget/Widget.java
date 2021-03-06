package com.yarten.widget;

import android.graphics.Color;

import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;

/**
 * Created by yfic on 2018/1/10.
 */

public class Widget
{
    /**
     * 用于显示的安卓控件的类型
     * 1. Button：按钮
     * 2. Rocker：遥感
     * 3. TouchPad: 触摸板
     */
    public enum Type
    {
        Button, Rocker, TouchPad, Orientation, Accelerometer, Vector, Undefined
    }

    public static final int WIDGET_LENGTH = 100;
    public static final int BUTTON_LENGTH = 100;
    public static final int ROCKER_LENGTH = 150;
    public static final int TOUCHPAD_LENGTH = 200;
    public static Style BUTTON_STYLE = new Style();
    public static Style ROCKER_STYLE = new Style();
    public static Style TOUCHPAD_STYLE = new Style();
    public static Style ORIENTATION_STYLE = new Style();
    public static Style ACCELEROMETER_STYLE = new Style();

    static
    {
        BUTTON_STYLE.color = Styleable.Color.Blue;
        BUTTON_STYLE.shape = Styleable.Shape.Circle;
        ROCKER_STYLE.color = Styleable.Color.Qing;
        ROCKER_STYLE.shape = Styleable.Shape.Circle;
        TOUCHPAD_STYLE.color = Styleable.Color.Dark;
        TOUCHPAD_STYLE.shape = Styleable.Shape.Square;
        ORIENTATION_STYLE.color = Styleable.Color.Purple;
        ORIENTATION_STYLE.shape = Styleable.Shape.Circle;
        ACCELEROMETER_STYLE.color = Styleable.Color.Red;
        ACCELEROMETER_STYLE.shape = Styleable.Shape.Circle;
    }

    public Type type;
    public String name;
    public String description;
    public Style style;

    public Widget(){}

    public Widget(String name, Type type, Style style, String description)
    {
        this.name = name;
        this.type = type;
        this.style = style;
        this.description = description;
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