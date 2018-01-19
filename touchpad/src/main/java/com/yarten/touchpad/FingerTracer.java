package com.yarten.touchpad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.yarten.shapebutton.ShapeView;
import com.yarten.utils.FiniteLinkedList;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_1_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_3_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;


/**
 * Created by yfic on 2018/1/18.
 */

public class FingerTracer extends View
{
    private class Vertex
    {
        float x, y;

        Vertex(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }

    private SparseArray<Vertex> traces = new SparseArray<>();
    private int maxPointers = 1, color;
    private float radius = 5;
    private Paint paint;

    public FingerTracer(Context context)
    {
        this(context, null);
    }

    public FingerTracer(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FingerTracer);
        setPointersNumber(ta.getInteger(R.styleable.FingerTracer_PointersNumber, 1));
        radius = ta.getDimensionPixelSize(R.styleable.FingerTracer_PointerRadius, Utils.dip2px(context, 20));
        setPointerColor(ta.getColor(R.styleable.FingerTracer_PointerColor, 0xDDDDDDDD));
        ta.recycle();

        paint = new Paint();
    }

    public void setPointersNumber(int number)
    {
        maxPointers = number;
    }

    public void setPointerColor(int color)
    {
        this.color = color;
    }

    private boolean isEnabled = true;

    public void setEnabled(boolean enabled){isEnabled = enabled;}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if(!isEnabled) return result;

        float x = event.getX(), y = event.getY();
        int pointer = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked())
        {
            case ACTION_DOWN:
            case ACTION_POINTER_DOWN:
                onDown(pointer, x, y);
                break;
            case ACTION_MOVE:
                onMove(pointer, x, y);
                break;
            case ACTION_UP:
            case ACTION_POINTER_UP:
            case ACTION_CANCEL:
                onUp(pointer);
                break;
        }

        return true;
    }

    private void onDown(int pointer, float x, float y)
    {
        if(traces.size() == maxPointers) return;
        traces.append(pointer, new Vertex(x, y));
        postInvalidate();
    }

    private void onMove(int pointer, float x, float y)
    {
        Vertex vertex = traces.get(pointer);
        if(vertex == null) return;
        vertex.x = x;
        vertex.y = y;
        postInvalidate();
    }

    private void onUp(int pointer)
    {
        traces.remove(pointer);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        for(int i = 0, size = traces.size(); i < size; i++)
        {
            Vertex vertex = traces.valueAt(i);
            paint.setColor(color);
            canvas.drawCircle(vertex.x, vertex.y, radius, paint);
        }
    }
}
