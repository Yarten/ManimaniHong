package com.yarten.touchpad;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.yarten.shapebutton.ShapeButton;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;

import java.util.List;
import java.util.Vector;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yfic on 2018/1/18.
 * 触摸板，手指在上面滑动的速度就是输出。
 * Styleable的定制：
 * 1. 颜色
 * 2. 不可改变形状
 */

public class TouchPad extends ConstraintLayout implements Controllable, Styleable<TouchPad>
{
    public TouchPad(Context context)
    {
        this(context, null);
    }

    public TouchPad(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.touchpad_layout, this);
        shapeButton = findViewById(R.id.shapeButton);
        shapeButton.setEnabled(false);
        fingerTracer = findViewById(R.id.fingerTracer);

        values = new Vector<>();
        values.add(0f);
        values.add(0f);
    }

    private ShapeButton shapeButton;
    private FingerTracer fingerTracer;
    private Vector<Float> values;
    private float lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float curX = event.getX();
        float curY = event.getY();
        float width = getWidth();
        float height = getHeight();
        float length = width < height ? width : height;

        switch (event.getAction())
        {
            case ACTION_DOWN:
                if(listener != null)
                    listener.onDown();
                break;
            case ACTION_MOVE:
                if(listener != null)
                {
                    values.set(0, (curX - lastX) / length);
                    values.set(1, (curY - lastY) / length);
                    listener.onMove(values);
                }
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                if(listener != null)
                    listener.onUp();
                break;
        }

        lastX = curX;
        lastY = curY;

        Log.i("TouchPad","TouchPad");
        return true;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        super.onInterceptTouchEvent(ev);
//        return true;
//    }

    private OnTouchListener onTouchListener;

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener)
    {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        onTouchEvent(ev);
        if(onTouchListener != null)
            onTouchListener.onTouch(this, ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    //region Styleable的设置
    @Override
    public TouchPad setColor(int color) {
        shapeButton.setColor(color);
        return this;
    }

    @Override
    public TouchPad setShape(Shape shape) {
        return this;
    }

    @Override
    public TouchPad setText(String text) {
        shapeButton.setText(text);
        return this;
    }

    @Override
    public TouchPad setBackgroundRotation(float rotation) {
        return this;
    }

    @Override
    public TouchPad setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
        return this;
    }

    @Override
    public Style getStyle() {
        Style style = shapeButton.getStyle();
        style.scale = getScaleX();
        return style;
    }

    @Override
    public TouchPad showText(boolean b) {
        shapeButton.showText(b);
        return this;
    }
    //endregion

    //region Controllable的配置
    private List<Controller> controllers;
    private Listener listener;

    @Override
    public List<Controller> getControllers() {
        return controllers;
    }

    @Override
    public void setControllers(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public Type getType() {
        return Type.Vector;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    //endregion
}
