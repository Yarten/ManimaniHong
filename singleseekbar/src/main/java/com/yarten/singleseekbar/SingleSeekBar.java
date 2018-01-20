package com.yarten.singleseekbar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by yfic on 2018/1/3.
 */

public class SingleSeekBar extends ConstraintLayout
{
    private TextView currentValue, minValue, maxValue;
    private SeekBar seekBar;

    public SingleSeekBar(Context context)
    {
        this(context, null);
    }

    public SingleSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.single_seekbar_layout, this);

        initTextView();
        initSeekBar();
    }

    private void initTextView()
    {
        currentValue = findViewById(R.id.currentValue);
        minValue = findViewById(R.id.minValue);
        maxValue = findViewById(R.id.maxValue);
    }

    private void initSeekBar()
    {
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress * 1.0f / 10000 * (max - min) + min;
                currentValue.setText(F2S(value));

                if(onValueChangedListener != null)
                    onValueChangedListener.onChange(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private float min, max;
    private String outputFormat;

    public void setRange(float min, float max)
    {
        setRange(min, max, 2);
    }

    public void setRange(float min, float max, int fixed)
    {
        this.min = min;
        this.max = max;
        outputFormat = "%." + fixed + "f";
        minValue.setText(F2S(min));
        maxValue.setText(F2S(max));
    }

    public void setCurrentValue(float value)
    {
        if(max == min) return;
        currentValue.setText(value + "");
        seekBar.setProgress((int)(10000 * (value - min) / (max - min)));
    }

    private String F2S(float value)
    {
        return String.format(outputFormat, value);
    }

    private OnValueChangedListener onValueChangedListener;

    public interface OnValueChangedListener
    {
        void onChange(float value);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener)
    {
        this.onValueChangedListener = onValueChangedListener;
    }
}
