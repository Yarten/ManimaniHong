package com.yarten.shapebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yarten.utils.Interface.Styleable;

/**
 * Created by yfic on 2017/12/26.
 */

public class ShapeView extends View
{
    public ShapeView(Context context)
    {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);

        switch (ta.getInt(R.styleable.ShapeView_shape, 0))
        {
            case 0:
                shape = Styleable.Shape.Square;
                break;
            case 1:
                shape = Styleable.Shape.Circle;
                break;
            case 2:
                shape = Styleable.Shape.Triangle;
                break;
        }

        setColor(ta.getColor(R.styleable.ShapeView_color, Color.Red));
        ta.recycle();
    }

    public static class Color
    {
        public static int Red = 0xFFFF0000;
    }

    private Styleable.Shape shape;

    private Paint paint = new Paint();
    private Paint paint2 = new Paint();

    public void setColor(int color)
    {
        paint.setColor(color - 0x70000000);
        paint2.setColor(color);
        postInvalidate();
    }

    public void setShape(Styleable.Shape shape)
    {
        this.shape = shape;
        postInvalidate();
    }

    public Styleable.Shape getShape()
    {
        return shape;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int width = getWidth();
        int height = getHeight();
        int length = width < height ? width : height;
        float halfWidth = 0.5f * width;
        float halfHeight = 0.5f * height;
        float half = 0.5f * length;

        // TODO: 清空canvas ?

        switch (shape)
        {
            case Square:
                canvas.drawRoundRect(halfWidth - half, halfHeight - half, halfWidth + half, halfHeight + half, 5, 5, paint);
                canvas.drawRoundRect(halfWidth - 0.7f * half, halfHeight - 0.7f * half, halfWidth + 0.7f * half, halfHeight + 0.7f * half, 5, 5, paint2);
                break;
            case Circle:
                canvas.drawCircle(halfWidth, halfHeight, half, paint);
                canvas.drawCircle(halfWidth, halfHeight, 0.8f * half, paint2);
                break;
            case Triangle:
                Path path = new Path();
                path.moveTo(halfWidth, halfHeight - half);
                path.lineTo(halfWidth + 0.866f * half, halfHeight + 0.5f * half);
                path.lineTo(halfWidth - 0.866f * half, halfHeight + 0.5f * half);
                path.close();
                canvas.drawPath(path, paint);
                Path path2 = new Path();
                path2.moveTo(halfWidth, halfHeight - 0.7f * half);
                path2.lineTo(halfWidth + 0.7f * 0.866f * half, halfHeight + 0.7f * 0.5f * half);
                path2.lineTo(halfWidth - 0.7f * 0.866f * half, halfHeight + 0.7f * 0.5f * half);
                path2.close();
                canvas.drawPath(path2, paint2);
                break;
        }
    }
}
