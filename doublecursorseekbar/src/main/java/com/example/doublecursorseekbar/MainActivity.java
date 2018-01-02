package com.example.doublecursorseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MySeekBar mySeekBar;
    private TextView leftCursor,rightCursor;
    float leftX,rightX,count;
    private SeekBar seekBar;

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
        seekBar = findViewById(R.id.DoubleCursor);
        seekBar.setRange(3,5);
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
