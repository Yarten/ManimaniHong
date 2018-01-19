package com.yarten.rocker;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.yarten.shapebutton.ShapeButton;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;

import java.util.List;
import java.util.Vector;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yfic on 2018/1/10.
 * SGRocker类 —— 摇杆控件
 *
 */

public class Rocker extends ConstraintLayout implements Styleable<Rocker>, Controllable
{
    //region 构造器
    public Rocker(Context context)
    {
        this(context, null);
    }

    public Rocker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.rocker_layout, this);
        initView();

        values = new Vector<>();
        values.add(0f);
        values.add(0f);
    }

    public boolean smooth = false;
    private final float failScale = 0.2f;
    private final float boundScale = 0.4f;
    private final float furthestOffsetScale = 1.5f + boundScale;
    private Vector<Float> values;
    //endregion

    //region 控件初始化
    private ShapeButton bigCircle, smallCircle;

    private void initView()
    {
        bigCircle = findViewById(R.id.big_circle);
        smallCircle = findViewById(R.id.small_circle);
        smallCircle.setShape(Shape.Circle);
        bigCircle.showText(false);
        bigCircle.setEnabled(false);
        smallCircle.setEnabled(false);
        bigCircle.setColor(0xAA000000);
    }
    //endregion

    //region 事件管理
    private float lastX, lastY, intX, intY;
    private boolean isDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //region 预处理
        final float R = super.getWidth() * 0.5f;
        final float radius = bigCircle.getWidth() * 0.5f;
        float curX = event.getX() - R;
        float curY = event.getY() - R;
        float distance = distanceToCenter(curX, curY);
        float rad = radToCenter(curX, curY, distance);
        //endregion

        switch (event.getAction())
        {
            case ACTION_DOWN:
                //region Case：按下摇杆
            {
                lastX = curX; lastY = curY;
                intX = intY = 0;
            } // no break on purpose
                //endregion

            case ACTION_MOVE:
                //region Case：摇杆移动
            {
                //region 平滑处理（不使摇杆跳跃到触点）
                if(smooth)
                {
                    intX += (curX - lastX); intY += (curY - lastY);
                    lastX = curX; lastY = curY;
                    curX = intX; curY = intY;
                }
                //endregion

                if(distance >= radius * furthestOffsetScale)
                    //region 触点离中心太远，视作离开摇杆区域
                    isDown = false;
                    //endregion
                else
                    //region 触点在摇杆区域中，再分摇杆内外处理
                {
                    if(isDown && distance >= radius * boundScale)
                        //region 当触屏区域不在活动范围内控制小球落在边沿
                    {
                        distance = radius * boundScale;
                        curX = radius * (float)(Math.sin(rad)) * boundScale;
                        curY = radius * (float)(Math.cos(rad)) * boundScale;
                    }   //endregion
                    else
                        //region 小球在活动范围内，直接等于当前触点位置
                        isDown = true;
                        //endregion
                }   //endregion
            } break;
            //endregion

            case ACTION_UP:
                //region Case：离开摇杆
                isDown = false;
                break;
                //endregion
        }

        //region 如果被判断为远离控件，则恢复控件初始状态
        if(!isDown)
        {
            curX = 0; curY = 0;
            distance = 0; rad = (float)(Math.PI);
        }
        //endregion

        //region 将小圆移动到计算后的位置
        setSmallPosition(curX, curY, R);
        //endregion

        //region 判断如果移动太小，则视为没有移动
        if(distance - failScale * radius < 0)
            curX = curY = 0;
        //endregion

        //region 回调监听器
        if(listener != null)
        {
            values.set(0, curX / (radius * boundScale));
            values.set(1, curY / (radius * boundScale));
            listener.onMove(values);
        }
        //endregion

        return true;
    }

    private void setSmallPosition(float px, float py, float R)
    {
        float r = smallCircle.getWidth() * 0.5f;
        smallCircle.setX(px - r + R);
        smallCircle.setY(py - r + R);
    }

    private float distanceToCenter(float x, float y)
    {
        return (float)Math.sqrt(x*x + y*y);
    }

    private float radToCenter(float x, float y, float distance)
    {
        float cosAngle = y / distance;
        float rad = (float)Math.acos(cosAngle);
        if(x < 0) rad = -rad;
        return rad;
    }
    //endregion

    //region Styleable的配置
    private int mColor;

    @Override
    public Rocker setColor(int color) {
        smallCircle.setColor(color);
        mColor = color;
        return this;
    }

    @Override
    public Rocker setShape(Shape shape) {
        bigCircle.setShape(shape);
        return this;
    }

    @Override
    public Rocker setText(String text) {
        smallCircle.setText(text);
        return this;
    }

    @Override
    public Rocker setBackgroundRotation(float rotation) {
        bigCircle.setBackgroundRotation(rotation);
        return this;
    }

    @Override
    public Rocker setScale(float scale) {
        super.setScaleX(scale);
        super.setScaleY(scale);
        return this;
    }

    @Override
    public Style getStyle() {
        Style style = new Style();
        Style bigStyle = bigCircle.getStyle();
        Style smallStyle = smallCircle.getStyle();
        style.color = mColor;
        style.shape = bigStyle.shape;
        style.text = smallStyle.text;
        style.x = getX();
        style.y = getY();
        style.scale = getScaleX();
        style.rotation = bigStyle.rotation;
        return style;
    }

    @Override
    public Rocker showText(boolean b) {
        smallCircle.showText(b);
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
