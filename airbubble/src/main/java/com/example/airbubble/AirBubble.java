package com.example.airbubble;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/1/1.
 */

public class AirBubble extends ConstraintLayout {

    private ConstraintLayout bubbleLayout;
    private TextView description;

    public AirBubble(Context context) {
        this(context,null);
    }

    public AirBubble(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.air_bubble,this);

        ini();
    }

    private void ini() {
        bubbleLayout = findViewById(R.id.BubbleLayout);
        description = findViewById(R.id.Description);
    }

    public void setText(String buttonDescription) {
        this.description.setText(buttonDescription);
    }

    public String getText() {
        return description.getText().toString();
    }
}
