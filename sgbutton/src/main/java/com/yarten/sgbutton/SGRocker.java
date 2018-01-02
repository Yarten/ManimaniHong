package com.yarten.sgbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import static android.content.ContentValues.TAG;

/**
 * Created by yarten on 2017/10/5.
 * SGRocker类 —— 摇杆控件
 * 组成：
 * 1. 背景，圆环，可以设置颜色与半径
 * 2. 前景，小圆，可以设置颜色
 */

public class SGRocker extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    //region 构造器
    private SurfaceHolder sfh;
    private Paint bgPainter, fgPainter;
    private MathFunction mathFunction;
    private final boolean debug = false;

    public SGRocker(Context context)
    {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        sfh.setFormat(PixelFormat.TRANSLUCENT);
        super.setZOrderOnTop(true);

        setFocusable(true);
        setFocusableInTouchMode(true);

        initPainter();
        setRadius(300);

        mathFunction = new ExponentialFunction(1000, 0, 1, 2, 0.3f);
    //    mathFunction = new LinearFunction(1000, 0, 1);
    }

    private void initPainter()
    {
        bgPainter = new Paint();
        bgPainter.setAntiAlias(true);
        bgPainter.setStyle(Paint.Style.STROKE);
        float r = radius * 0.25f;
        bgPainter.setStrokeWidth(r);
        bgPainter.setColor(backgroundColor);

        fgPainter = new Paint();
        fgPainter.setAntiAlias(true);
        fgPainter.setStyle(Paint.Style.FILL);
        fgPainter.setColor(foregroundColor);
    }
    //endregion

    //region 渲染线程控制
    private boolean isRunning;

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        isRunning = true;
        rockerX = 0;
        rocketY = 0;
        mathFunction.ready();
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int widht, int height)
    {
        // TODO
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        isRunning = false;
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            if(debug) draw2();
            else draw();
            try{Thread.sleep(10);}
            catch (Exception e){}
        }
    }
    //endregion

    //region 渲染线程
    private Canvas canvas;

    private final float offsetScale = 1.6f;

    private float dScale = 0.0f;

    private boolean showing = false;

    private void draw()
    {
        canvas = sfh.lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        float offset = radius*(offsetScale-1);

        if(!effect) dScale = 1.0f;
        else dScale = mathFunction.timing();

        float D = 2*radius; //*dScale;
        float E = 0; //2*radius*(1-dScale);

        // 绘制背景
        RectF rectF = new RectF(offset+E, offset+E, offset+D, offset+D);
        bgPainter.setAlpha((int)(dScale*255));

        for(int i = 0; i < 4; i++)
        {
            if(dScale > 1.0f) dScale = 5.0f;
            float angle = -30 + 90*i+ 120*dScale;
            if(angle > 180) angle -= 360;
            if(angle < -180) angle += 360;

            canvas.drawArc(rectF, angle, 60, false, bgPainter);
        }

        if(!showing) mathFunction.direction(false);
        else mathFunction.direction(true);

        // 绘制前景
        offset = radius*offsetScale;
        canvas.drawCircle(rockerX+offset, rocketY+offset, radius*0.6f, fgPainter);

        if(canvas != null) sfh.unlockCanvasAndPost(canvas);
    }

    //region Debug Mode for Drawing
    private void draw2()
    {
        canvas = sfh.lockCanvas();
        canvas.drawColor(Color.RED);

        float offset = radius*(offsetScale-1);

        if(!effect) dScale = 1.0f;
        else dScale = mathFunction.timing();

        float D = 2*radius; //*dScale;
        float E = 0; //2*radius*(1-dScale);

        // 绘制背景
        RectF rectF = new RectF(offset+E, offset+E, offset+D, offset+D);
        bgPainter.setAlpha((int)(dScale*255));

        for(int i = 0; i < 4; i++)
        {
            if(dScale > 1.0f) dScale = 5.0f;
            float angle = -30 + 90*i+ 120*dScale;
            if(angle > 180) angle -= 360;
            if(angle < -180) angle += 360;

            canvas.drawArc(rectF, angle, 60, false, bgPainter);
        }

        if(!showing) mathFunction.direction(false);
        else mathFunction.direction(true);

        // 绘制前景
        offset = radius*offsetScale;
        canvas.drawCircle(rockerX+offset, rocketY+offset, radius*0.6f, fgPainter);

        float w = (float)canvas.getWidth();
        float h = (float)canvas.getHeight();

        Paint tempPainter = new Paint();
        tempPainter.setStyle(Paint.Style.STROKE);
        tempPainter.setStrokeWidth(10);
        tempPainter.setColor(Color.GREEN);
        canvas.drawLine(0, 0, w, h, tempPainter);
        canvas.drawLine(w, 0, 0, h, tempPainter);

        if(canvas != null) sfh.unlockCanvasAndPost(canvas);
    }
    //endregion


    //endregion

    //region 颜色，半径等设置
    private int backgroundColor = 0xff8bc5ba;
    private int foregroundColor = 0xf84c4cb8;

    private float radius;

    public void setRadius(float radius)
    {
        this.radius = radius;
        float r = radius * 0.25f;
        bgPainter.setStrokeWidth(r);

        float D = 2*radius*offsetScale;
        sfh.setFixedSize((int)D, (int)D);
    }

    public float getRadius(){return radius;}

    public void setBackgroundColor(int color)
    {
        backgroundColor = color;
        bgPainter.setColor(color);
    }

    public int getBackgroundColor(){return backgroundColor;}

    public void setForegroundColor(int color)
    {
        foregroundColor = color;
        fgPainter.setColor(color);
    }

    public int getForegroundColor(){return foregroundColor;}

    public boolean smooth = false;

    public boolean effect = true;
    //endregion

    //region 触摸事件管理
    public interface OnRockerListener
    {
        /**
         * @Title OnAction
         * @param d: 归一化到0~100的径向距离
         * @param angle: 从垂直向上往顺时针转过的角度
         */
        void onAction(float d, float angle);
    }

    private OnRockerListener onRockerListener;

    public void setOnRockerListener(OnRockerListener onRockerListener)
    {
        this.onRockerListener = onRockerListener;
    }

    private float rockerX, rocketY;
    private float lastX, lastY;
    private float lastingX, lastingY;
    private final float rad2angle = (float)(180.0 / Math.PI);
    private boolean isDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //region 预处理
        int action = event.getAction();
        float R = offsetScale * radius;
        float curX = event.getX() - R;
        float curY = event.getY() - R;
        //endregion

        // 得到触点到中心的距离
        float distance = length(curX, curY);
        // 得到摇杆中心与触屏点所形成的角度
        float rad = getRad(curX, curY);

        switch (action)
        {
            //region Case：按下摇杆
            case MotionEvent.ACTION_DOWN:
            {
                lastX = curX;
                lastY = curY;
                lastingX = lastingY = 0;
            } // no break on purpose
            //endregion

            //region Case：摇杆移动
            case MotionEvent.ACTION_MOVE:
            {
                //region 平滑处理（不使摇杆跳跃到触点）
                if(smooth)
                {
                    lastingX += (curX-lastX);
                    lastingY += (curY-lastY);
                    lastX = curX;
                    lastY = curY;
                    curX = lastingX;
                    curY = lastingY;
                }
                //endregion

                if(distance >= radius*2.0)
                    //region 触点离中心太远，视作离开摇杆区域
                {
                    isDown = false;
                }   //endregion
                else
                    //region 触点在摇杆区域中，再分摇杆内外处理
                {
                    if(isDown && distance >= radius)
                    //region 当触屏区域不在活动范围内控制小球落在边沿
                    {
                        // 将距离置为半径
                        distance = radius;
                        //保证内部小圆运动的长度限制
                        getXYonCircle(rad);
                    }   //endregion，
                    else
                    //region 小球在活动范围内，直接等于当前触点位置
                    {
                        isDown = true;
                        rockerX = curX;
                        rocketY = curY;
                    }   //endregion
                }   //endregion
            } break;
            //endregion

            //region Case：离开摇杆
            case MotionEvent.ACTION_UP:
            {
                isDown = false;
            } break;
            //endregion
        }

        //region 按压响应（根据按压与否设置相关值）
        if(isDown) showing = true;
        else
        {
            showing = false;
            rockerX = 0;
            rocketY = 0;
            distance = 0;
            rad = (float)(Math.PI);
        }
        //endregion

        //region 调用监听器
        if(onRockerListener != null)
        {
            final float failScale = 0.1f;
            distance = (distance - failScale*radius);
            if(distance < 0) distance = 0;
            distance = 100 * distance / ((1-failScale)*radius);
            onRockerListener.onAction(distance, 180.0f - rad * rad2angle);
        }
        //endregion

        return true;
    }

    // 得到两点间弧度
    private float getRad(float x, float y)
    {
        float z = length(x, y);
        float cosAngle = y / z;
        float rad = (float)Math.acos(cosAngle);
        if(x < 0) rad = -rad;
        return rad;
    }

    private void getXYonCircle(float rad)
    {
        rockerX = (float)(radius * Math.sin(rad));
        rocketY = (float)(radius * Math.cos(rad));
    }

    private float length(float a, float b)
    {
        return (float)Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }
    //endregion
}
