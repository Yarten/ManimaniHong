package com.yarten.editor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yfic on 2017/12/31.
 */

public class ColorRadioButton extends AppCompatRadioButton
{
    private ColorPicker.Listener listener = null;

    public ColorRadioButton(Context context)
    {
        this(context, null);
    }

    public ColorRadioButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setButtonDrawable(android.R.drawable.alert_dark_frame);

        states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_enabled};

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onPick(color);
            }
        });

        setColor(Color.BLACK);
    }

    private int[][] states;
    private int color;

    public ColorRadioButton setColor(int color)
    {
        int[] colors = new int[]{color - 0xA0000000, color};
        this.color = color;

        setButtonTintList(new ColorStateList(states, colors));

        postInvalidate();
        return this;
    }

    public ColorRadioButton setListener(ColorPicker.Listener listener)
    {
        this.listener = listener;
        return this;
    }

}
