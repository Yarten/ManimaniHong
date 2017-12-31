package com.yarten.editor;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Interface.Touchable;

import java.util.Vector;

/**
 * Created by yfic on 2017/12/31.
 */

public class ShapePicker extends ConstraintLayout
{
    public interface Listener
    {
        void onPick(Styleable.Shape shape);
    }

    public ShapePicker(Context context)
    {
        this(context, null);
    }

    public ShapePicker(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ShapePicker(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.shape_picker, this);

        triangle = findViewById(R.id.shape_triangle);
        square = findViewById(R.id.shape_square);
        circle = findViewById(R.id.shape_circle);
    }

    public ShapePicker setListener(Listener listener)
    {
        setListener(triangle, listener, Styleable.Shape.Triangle);
        setListener(square, listener, Styleable.Shape.Square);
        setListener(circle, listener, Styleable.Shape.Circle);
        return this;
    }

    private ShapeButton triangle, square, circle;

    private void setListener(ShapeButton button, final Listener listener, final Styleable.Shape shape)
    {
        button.setListener(new Touchable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {
                listener.onPick(shape);
            }

            @Override
            public void onMove(Vector<Float> values) {

            }
        });
    }
}
