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

    private ConstraintLayout bubbleLayout;
    private ShapeButton triangle;
    private TextView description;
    private Context context;

    public AirBubble(Context context) {
        this(context,null);
    }

    public AirBubble(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.air_bubble,this);

        ini();
    }

    private void ini() {
        bubbleLayout = findViewById(R.id.BubbleLayout);
        triangle = findViewById(R.id.shapeButton);
        description = findViewById(R.id.Description);
    }

    public void setText(String buttonDescription) {
        this.description.setText(buttonDescription);
    }

    public String getText() {
        return description.getText().toString();
    }

    public void setWidth(int width) {
        int percentage = bubbleLayout.getLayoutParams().width / width;
        bubbleLayout.getLayoutParams().width = width;
        triangle.getLayoutParams().width = triangle.getWidth() / percentage;
        description.getLayoutParams().width = description.getWidth() / percentage;
    }

    public void setHeight(int height) {
        int percentage = bubbleLayout.getLayoutParams().height / height;
        bubbleLayout.getLayoutParams().height = height;
        triangle.getLayoutParams().height = triangle.getHeight() / percentage;
        description.getLayoutParams().height = description.getHeight() / percentage;
    }

    public void setSize(int width, int height) {
        int percentage = bubbleLayout.getLayoutParams().width / width;
        bubbleLayout.getLayoutParams().width = width;
        triangle.getLayoutParams().width = triangle.getWidth() / percentage;
        description.getLayoutParams().width = description.getWidth() / percentage;
        bubbleLayout.getLayoutParams().height = height;
        triangle.getLayoutParams().height = triangle.getHeight() / percentage;
        description.getLayoutParams().height = description.getHeight() / percentage;
    }

    public int returnWidth() {
        return bubbleLayout.getLayoutParams().width;
    }

    public int returnHeight() {
        return bubbleLayout.getLayoutParams().height;
    }

    public void setTextAlignCenter() {
        description.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }



    public void setBubbleLayout(int Direction, float Percentage) {
        if (Percentage <= 0.35) Percentage = (float)0.35;
        if (Percentage >= 1.78) Percentage = (float)1.78;
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

}
