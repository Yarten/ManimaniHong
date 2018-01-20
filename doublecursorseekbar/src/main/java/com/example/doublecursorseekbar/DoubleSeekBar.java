package com.example.doublecursorseekbar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.rangebar.RangeBarEx;

/**
 * Created by lenovo on 2018/1/1.
 * 双端seekbar
 */

public class DoubleSeekBar extends ConstraintLayout {

    //region 定义成员变量
    private RangeBarEx rangeBar;
    private TextView leftCursor,rightCursor;
    private float min,max;
    //endregion

    public DoubleSeekBar(Context context) {
        this(context,null);
    }

    public DoubleSeekBar(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.double_seekbar, this);

        ini();
    }

    private void ini()
    {
        rangeBar = findViewById(R.id.rangeBarEx);
        leftCursor = findViewById(R.id.LeftCursor);
        rightCursor = findViewById(R.id.RightCursor);
        min = 0;
        max = 0;
    }

    private String outputFormat = "%.2f";

    //设置最大最小值范围
    public void setRange(float min, float max) {
        setRange(min, max, 2);
    }

    public void setRange(float min, float max, int fixed)
    {
        this.min = min;
        this.max = max;
        outputFormat = "%." + fixed + "f";
        rangeBar.setRange(min, max);
        setCurrentValues(min, max);
    }

    public void setCurrentValues(float left, float right)
    {
        if(min == max) return;

        rangeBar.setCurrentValues(left, right);
        if(left < right)
            notifyValuesChange(left, right);
        else notifyValuesChange(right, left);
    }

    private void notifyValuesChange(float left, float right)
    {
        leftCursor.setText(String.format(outputFormat, left));
        rightCursor.setText(String.format(outputFormat, right));
    }

    //返回最小值
    public float getMin() {
        return min;
    }

    //返回最大值
    public float getMax() {
        return max;
    }

    //识别游标变化
    public interface OnValueChangedListener
    {
        void onChange(float min, float max);
    }

    private OnValueChangedListener onValueChangeListener;

    public void setOnValueChangeListener(OnValueChangedListener onValueChangeListener)
    {
        this.onValueChangeListener = onValueChangeListener;
        rangeBar.setOnRangeBarChangeListener(new RangeBarEx.OnRangeBarChangeListener()
        {
            @Override
            public void onRangeBarChange(float leftPinValue, float rightPinValue)
            {
                notifyValuesChange(leftPinValue, rightPinValue);
                DoubleSeekBar.this.onValueChangeListener.onChange(leftPinValue, rightPinValue);
            }
        });
    }
}
