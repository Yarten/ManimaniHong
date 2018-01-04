package com.yarten.utils;

import com.yarten.utils.Interface.Styleable;
import com.google.gson.Gson;

/**
 * Created by yfic on 2017/12/31.
 */

public class Style
{
    public int color;
    public Styleable.Shape shape;
    public String text;
    public float x;
    public float y;
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
