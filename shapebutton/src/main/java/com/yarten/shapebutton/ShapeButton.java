package com.yarten.shapebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarten.device.UCP.Controllable;
import com.yarten.device.UCP.Controller;
import com.yarten.device.UCP.Signal;
import com.yarten.sgbutton.SGWidget;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yfic on 2017/12/26.
 */

public class ShapeButton extends ConstraintLayout implements Styleable<ShapeButton>, Controllable
{
    //region 构造器
    public ShapeButton(Context context)
    {
        this(context, null);
    }

    public ShapeButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ShapeButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        this.controllers = new ArrayList<>();
        this.controllers.add(new Controller(Signal.Type.Boolean, "Event"));

        LayoutInflater.from(context).inflate(R.layout.shape_button, this);

        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
        shapeView = findViewById(R.id.shape);

        values = new Vector<>();
        values.add(0f);
        values.add(0f);
        initStyle(context, attrs);
    //    initEvent();
    }

    private TextView textView;
    private ImageView imageView;
    private ShapeView shapeView;
    private Context context;

    private void initStyle(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeButton);

        switch (ta.getInt(R.styleable.ShapeButton_shape, 0))
        {
            case 0:
                setShape(Shape.Square);
                break;
            case 1:
                setShape(Shape.Circle);
                break;
            case 2:
                setShape(Shape.Triangle);
                break;
        }

        setColor(ta.getColor(R.styleable.ShapeButton_color, Color.Red));
        setRotation(ta.getInteger(R.styleable.ShapeButton_rotation, 0));
        setText(ta.getString(R.styleable.ShapeButton_text));
        setImage(ta.getResourceId(R.styleable.ShapeButton_image, 0));
    }
    //endregion

    //region 事件控制
    private void initEvent()
    {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case ACTION_DOWN:
                        onDown();
                        break;
                    case ACTION_MOVE:
                        onMove(event.getX(), event.getY());
                        break;
                    case ACTION_UP:
                    case ACTION_CANCEL:
                        onUp();
                        return true;
                }
                return false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onUp();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        switch (event.getAction())
        {
            case ACTION_DOWN:
                onDown();
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                onUp();
                break;
        }
        return true;
    }

    private void onDown()
    {
        shapeView.setColor(mColor - 0x70000000);
        if(listener != null)
            listener.onDown();
    }

    private void onUp()
    {
        shapeView.setColor(mColor);
        if(listener != null)
            listener.onUp();
    }

    private Vector<Float> values;

    private void onMove(float x, float y)
    {
        if(listener != null)
        {
            values.set(0, x);
            values.set(1, y);
            listener.onMove(values);
        }
    }

    //endregion


    //region Styleable设置
    private int mColor = 0xFFFF0000;

    @Override
    public ShapeButton setColor(int color) {
        shapeView.setColor(color);
        mColor = color;
        return this;
    }

    @Override
    public ShapeButton setShape(Shape shape) {
        shapeView.setShape(shape);
        return this;
    }

    @Override
    public ShapeButton setText(String text) {
        textView.setText(text);
        return this;
    }

    @Override
    public ShapeButton setBackgroundRotation(float rotation) {
        shapeView.setRotation(rotation);
        return this;
    }

    @Override
    public ShapeButton showText(boolean b) {
        textView.setVisibility(b ? View.VISIBLE : View.GONE);
        return this;
    }

    public ShapeButton setImage(int reference)
    {
        if(reference != 0)
            imageView.setImageDrawable(context.getDrawable(reference));
        return this;
    }

    @Override
    public ShapeButton setScale(float scale) {
        super.setScaleX(scale);
        super.setScaleY(scale);
        return this;
    }

    @Override
    public Style getStyle() {
        Style style = new Style();
        style.color = mColor;
        style.shape = shapeView.getShape();
        style.text = textView.getText().toString();
        style.x = getX();
        style.y = getY();
        style.scale = getScaleX();
        style.rotation = shapeView.getRotation();
        return style;
    }

    //endregion

    //region Controller设置
    private List<Controller> controllers;

    @Override
    public List<Controller> getControllers() {
        return controllers;
    }

    private Listener listener = null;

    @Override
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    //endregion
}
