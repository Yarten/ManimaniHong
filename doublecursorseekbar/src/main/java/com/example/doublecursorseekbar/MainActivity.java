package com.example.doublecursorseekbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.airbubble.AirBubble;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private MySeekBar mySeekBar;
    private TextView leftCursor,rightCursor;
    float leftX,rightX,count;
    private SeekBar seekBar;
    private AirBubble airBubble,airBubble2,airBubble3,airBubble4;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ini();
//        notifySeekbarChanged(1,5);
    }

    private void ini() {
//        mySeekBar = findViewById(R.id.MySeekBar);
//        leftCursor = findViewById(R.id.LeftCursor);
//        rightCursor = findViewById(R.id.RightCursor);
//        seekBar = findViewById(R.id.DoubleCursor);
//        seekBar.setRange(3,5);


        airBubble = new AirBubble(this);
        airBubble.setText("This is my bubble.This is my bubble.This is my bubble.This is my bubble.This is my bubble.");

        airBubble2 = new AirBubble(this);
        airBubble2.setText("This is my bubble.This is my bubble.This is my bubble.This is my bubble.This is my bubble.");

        airBubble3 = new AirBubble(this);
        airBubble3.setText("This is my bubble.This is my bubble.This is my bubble.This is my bubble.This is my bubble.");

        airBubble4 = new AirBubble(this);
        airBubble4.setText("This is my bubble.This is my bubble.This is my bubble.This is my bubble.This is my bubble.");

        layout = findViewById(R.id.Layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        height = airBubble.returbHeight();

        airBubble.setBubbleLayout(Gravity.LEFT, (float)(0.5));

        airBubble2.setY((float)(height * 1.5));
        airBubble2.setBubbleLayout(Gravity.RIGHT,(float)0.5);

        airBubble3.setY((float)(height * 3));
        airBubble3.setBubbleLayout(Gravity.TOP,(float)0.5);

        airBubble4.setY((float)(height * 4.5));
        airBubble4.setBubbleLayout(Gravity.BOTTOM,(float)1.5);

        layout.addView(airBubble);
        layout.addView(airBubble2);
        layout.addView(airBubble3);
        layout.addView(airBubble4);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    //    private void notifySeekbarChanged(final float min, final float max) {
//        mySeekBar.setListener(new MySeekBar.OnSeekFinishListener() {
//            @Override
//            public void seekPos(MySeekBar.CircleIndicator left, MySeekBar.CircleIndicator right) {
//                leftX = mySeekBar.leftX;
//                rightX = mySeekBar.rightX;
//                count = rightX - leftX;
//                float newLeftX = (mySeekBar.mLeftCI.getCurX() - leftX) / count * (max - min) + min;
//                float newRightX = (mySeekBar.mRightCI.getCurX() - leftX) / count * (max - min) + min;;
//                leftCursor.setText(newLeftX + "");
//                rightCursor.setText(newRightX + "");
//            }
//        });
//    }
}
