package com.example.doublecursorseekbar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/1/1.
 */

public class DoubleSeekBar extends ConstraintLayout {

    //region 定义成员变量
    private ConstraintLayout seekbarLayout;
    private MySeekBar mySeekBar;
    private TextView leftCursor,rightCursor;
    private TextView minValue, maxValue;
    private float leftX,rightX,count;
    private float min,max;
    //endregion

    public DoubleSeekBar(Context context) {
        this(context,null);
    }

    public DoubleSeekBar(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.myseekbar, this);

        ini();
        notifySeekbarChanged();
    }

    private void ini() {
        seekbarLayout = findViewById(R.id.SeekbarLayout);
        mySeekBar = findViewById(R.id.MySeekBar);
        leftCursor = findViewById(R.id.LeftCursor);
        rightCursor = findViewById(R.id.RightCursor);
        minValue = findViewById(R.id.minValue);
        maxValue = findViewById(R.id.maxValue);
        min = 0;
        max = 0;
    }

    //返回左游标的值
    public float getLeftBound() {
        return leftX;
    }

    //返回右游标的值
    public float getRightBound() {
        return rightX;
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
        minValue.setText(String.format(outputFormat, fixed, min));
        maxValue.setText(String.format(outputFormat, fixed, max));
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
    public void notifySeekbarChanged() {
        mySeekBar.setListener(new MySeekBar.OnSeekFinishListener() {
            @Override
            public void seekPos(MySeekBar.CircleIndicator left, MySeekBar.CircleIndicator right) {
                leftX = mySeekBar.leftX;
                rightX = mySeekBar.rightX;
                count = rightX - leftX;
                float newLeftX = (mySeekBar.mLeftCI.getCurX() - leftX) / count * (max - min) + min;
                float newRightX = (mySeekBar.mRightCI.getCurX() - leftX) / count * (max - min) + min;
                leftCursor.setText(String.format(outputFormat, newLeftX));
                rightCursor.setText(String.format(outputFormat, newRightX));
                if(onValueChangeListener != null)
                    onValueChangeListener.onChange(newLeftX, newRightX);
            }
        });
    }

    public interface OnValueChangedListener
    {
        void onChange(float min, float max);
    }

    private OnValueChangedListener onValueChangeListener;

    public void setOnValueChangeListener(OnValueChangedListener onValueChangeListener)
    {
        this.onValueChangeListener = onValueChangeListener;
    }
}
