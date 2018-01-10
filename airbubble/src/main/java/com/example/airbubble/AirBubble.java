package com.example.airbubble;

import android.content.Context;
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

    public int returbHeight() {
        return description.getLayoutParams().height;
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
