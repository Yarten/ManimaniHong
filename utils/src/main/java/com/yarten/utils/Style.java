package com.yarten.utils;

import com.yarten.utils.Interface.Styleable;
import com.google.gson.Gson;

/**
 * Created by yfic on 2017/12/31.
 * UI控件风格，包含了：
 * 1. 颜色（由Styleable的Color提供）
 * 2. 形状（由Styleable的Shape提供）
 * 3. 文本：控件的标题；
 * 4. 坐标：控件在遥控器下的坐标，单位为PX，跟手机相关。
 * TODO: 要做到不同手机，不同大小屏幕都适配
 * 5. 缩放：控件相对于其原本设定的大小的缩放；
 * 6. 旋转：控件背景的旋转（而并不是整个控件的旋转）
 */

public class Style
{
    public int color = Styleable.Color.Red;
    public Styleable.Shape shape = Styleable.Shape.Default;
    public String text = "None";
    public float x = 0;
    public float y = 0;
    public float scale = 1.0f;
    public float rotation = 0;

    // 将Style对象转换为JSON
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public Style clone()
    {
        Style style = new Style();
        style.color = color;
        style.shape = shape;
        style.text = text;
        style.x = x;
        style.y = y;
        style.scale = scale;
        style.rotation = rotation;
        return style;
    }

    public void stylize(Styleable styleable)
    {
        styleable.setText(text);
        styleable.setColor(color);
        styleable.setShape(shape);
        styleable.setScale(scale);
        styleable.setBackgroundRotation(rotation);
    }
}
