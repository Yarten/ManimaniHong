package com.example.airbubble;

import android.content.Context;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.yarten.shapebutton.ShapeButton;
import com.yarten.shapebutton.ShapeView;
import com.yarten.utils.Utils;

/**
 * Created by lenovo on 2018/1/1.
 */

public class AirBubble extends ConstraintLayout {

    //region 变量定义
    private ConstraintLayout bubbleLayout;
    private ShapeButton triangle;
    private TextView description;
    private Context context;
    //endregion

    //region 构造函数
    public AirBubble(Context context) {
        this(context,null);
    }

    public AirBubble(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.air_bubble,this);

        ini();
    }
    //endregion

    //region 绑定控件
    private void ini() {
        bubbleLayout = findViewById(R.id.BubbleLayout);
        triangle = findViewById(R.id.shapeButton);
        description = findViewById(R.id.Description);
    }
    //endregion

    //region 设置气泡文字
    public void setText(String buttonDescription) {
        this.description.setText(buttonDescription);
    }
    //endregion

    //region 获取气泡文字
    public String getText() {
        return description.getText().toString();
    }
    //endregion

    //region 设置气泡宽度
    public void setWidth(int width) {
        int percentage = bubbleLayout.getLayoutParams().width / width;
        bubbleLayout.getLayoutParams().width = width;
        triangle.getLayoutParams().width = triangle.getWidth() / percentage;
        description.getLayoutParams().width = description.getWidth() / percentage;
    }
    //endregion

    //region 设置气泡高度
    public void setHeight(int height) {
        int percentage = bubbleLayout.getLayoutParams().height / height;
        bubbleLayout.getLayoutParams().height = height;
        triangle.getLayoutParams().height = triangle.getHeight() / percentage;
        description.getLayoutParams().height = description.getHeight() / percentage;
    }
    //endregion

    //region 设置气泡大小
    public void setSize(int width, int height) {
        int percentage = bubbleLayout.getLayoutParams().width / width;
        bubbleLayout.getLayoutParams().width = width;
        triangle.getLayoutParams().width = triangle.getWidth() / percentage;
        description.getLayoutParams().width = description.getWidth() / percentage;
        bubbleLayout.getLayoutParams().height = height;
        triangle.getLayoutParams().height = triangle.getHeight() / percentage;
        description.getLayoutParams().height = description.getHeight() / percentage;
    }
    //endregion

    //region 返回气泡宽度
    public int returnWidth() {
        return bubbleLayout.getLayoutParams().width;
    }
    //endregion

    //region 返回气泡高度
    public int returnHeight() {
        return bubbleLayout.getLayoutParams().height;
    }
    //endregion

    //region 设置气泡文字居中
    public void setTextAlignCenter() {
        description.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
    //endregion

    //region 设置气泡的形状（三角形的方向以及位置）
    public void setBubbleLayout(int Direction, float Percentage) {
        //设定三角形的位置范围
        if (Percentage <= 0.35) Percentage = (float)0.35;
        if (Percentage >= 1.78) Percentage = (float)1.78;

        /*
         * 如果设置三角形位于TextView的左边，则仅根据percentage更改三角形在y方向上的位置
         * 如果设置三角形位于TextView的右边，则更改三角形在x方向上的位置并旋转180度，并根据percentage设置三角形在y方向上的位置
         * 如果设置三角形位于TextView的上边，则更改三角形在y方向上的位置并旋转90度，并根据percentage设置三角形在x方向上的位置
         * 如果设置三角形位于TextView的下边，则更改三角形在y方向上的位置并旋转270度，并根据percentage设置三角形在x方向上的位置
         */
        if (Direction == Gravity.LEFT) {
            bubbleLayout.getLayoutParams().height = description.getLayoutParams().height;
            triangle.setY(description.getY());
//            triangle.setRotation(270);
        }
        if (Direction == Gravity.RIGHT) {
            bubbleLayout.getLayoutParams().height = description.getLayoutParams().height;
            triangle.setX(description.getX() + description.getLayoutParams().width - Utils.dip2px(context,10));
            triangle.setY(description.getY());
//            triangle.setRotation(90);
            triangle.setRotation(180);
        }
        if (Direction == Gravity.TOP) {
            triangle.setX((float)(description.getX() + description.getLayoutParams().width * 0.5 * Percentage -  Utils.dip2px(context,10)));
            triangle.setY(description.getY() - triangle.getLayoutParams().height / 2);
//            triangle.setRotation(0);
            triangle.setRotation(90);
        }
        if (Direction == Gravity.BOTTOM) {
            triangle.setX((float)(description.getX() + description.getLayoutParams().width * 0.5 * Percentage -  Utils.dip2px(context,10)));
            triangle.setY(description.getY() + triangle.getLayoutParams().height / 2);
//            triangle.setRotation(180);
            triangle.setRotation(270);
        }
    }
    //endregion

}
