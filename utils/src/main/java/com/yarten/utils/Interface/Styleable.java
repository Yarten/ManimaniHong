package com.yarten.utils.Interface;

import com.yarten.utils.Style;

/**
 * Created by yfic on 2017/12/26.
 */

public interface Styleable<T>
{
    enum Shape
    {
        Square, Circle, Triangle
    }

    class Color
    {
        public static int Red = 0xFFFF0000;
    }

    T setColor(int color);
    T setShape(Shape shape);
    T setText(String text);
    T setRotation(int rotation);

    Style getStyle();
    T showText(boolean b);
}
