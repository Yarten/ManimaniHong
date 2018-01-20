package com.example.rangebar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/1/18.
 */

public class RangeBarEx extends ConstraintLayout {

    public interface OnRangeBarChangeListener {
        void onRangeBarChange(float leftPinValue, float rightPinValue);
    }

    //region 变量定义
    private ConstraintLayout rangebarLayout;
    private RangeBar rangeBar;
    private TextView minValue,maxValue;
    //endregion

    //region 构造函数
    public RangeBarEx(Context context) {
        this(context,null);
    }

    public RangeBarEx(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.range_bar,this);

        ini();
    }
    //endregion

    //region 绑定控件并定义样式
    private void ini() {
        rangebarLayout = findViewById(R.id.rangeBarLayout);
        rangeBar = findViewById(R.id.rangeBar);
        minValue = findViewById(R.id.minValue);
        maxValue = findViewById(R.id.maxValue);

        rangeBar.setDrawTicks(false);                                           //取消bar中的小点
        rangeBar.setBarColor(0xFF000000);                                       //设置bar的颜色
        rangeBar.setPinColor(0xFFFF4081);                                       //设置游标的颜色
        rangeBar.setConnectingLineColor(0xFFFF4081);                            //设置游标间区间的颜色
        rangeBar.setSelectorColor(0xFFFF4081);                                  //设置气泡的颜色
        rangeBar.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String value) {                                      //限定气泡内保留两位小数
                return String.format("%.2f", Float.parseFloat(value) / 100);
            }
        });
    }
    //endregion

    //region 设置最大最小值
    public void setRange(float startValue, float endValue) {
        rangeBar.setTickStart(startValue * 100);
        rangeBar.setTickEnd(endValue * 100);
        minValue.setText(String.format("%.2f", startValue));
        maxValue.setText(String.format("%.2f",endValue));
    }
    //endregion

    //region 设置游标的位置
    public void setCurrentValues(float leftValue, float rightValue)
    {
        rangeBar.setRangePinsByValue(leftValue * 100, rightValue * 100);
    }
    //endregion

    //region 提供监听器借口
    public void setOnRangeBarChangeListener(final OnRangeBarChangeListener onRangeBarChangeListener) {
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue)
            {
                float left = Float.parseFloat(leftPinValue) / 100.0f;
                float right = Float.parseFloat(rightPinValue) / 100.0f;
                onRangeBarChangeListener.onRangeBarChange(left, right);
            }
        });
    }
    //endregion
}
