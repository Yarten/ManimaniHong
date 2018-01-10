package com.yarten.widget;

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
     */
    public enum Type
    {
        Button, Rocker, Undefined
    }

    public static final int WIDGET_LENGTH = 100;
    public static final int BUTTON_LENGTH = 100;
    public static final int ROCKER_LENGTH = 150;
    public static Style BUTTON_STYLE = new Style();
    public static Style ROCKER_STYLE = new Style();

    public Type type;
    public String name;
    public String description;
    public Style style;

    static
    {
        BUTTON_STYLE.color = Styleable.Color.Blue;
        BUTTON_STYLE.shape = Styleable.Shape.Circle;
        ROCKER_STYLE.color = Styleable.Color.Qing;
        ROCKER_STYLE.shape = Styleable.Shape.Circle;
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