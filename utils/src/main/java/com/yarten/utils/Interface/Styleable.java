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
        public static int Qing = 0xFF46B39E;
        public static int Green = 0xFF27AE61;
        public static int Blue = 0xFF297FB8;
        public static int Purple = 0xFF8D44AD;
        public static int Dark = 0xFF2D3E50;
        public static int Orange = 0xFFF39C11;
        public static int Red = 0xFFC1392B;
        public static int White = 0xFFBEC3C7;
        public static int Gray = 0xFF7E8C8D;

        public static final int[] colors = {
                Color.Qing, Color.Green, Color.Blue,
                Color.Purple, Color.Dark, Color.Orange,
                Color.Red, Color.White, Color.Gray
        };
    }

    T setColor(int color);
    T setShape(Shape shape);
    T setText(String text);
    T setBackgroundRotation(float rotation);
    T setScale(float scale);

    Style getStyle();
    T showText(boolean b);
}
