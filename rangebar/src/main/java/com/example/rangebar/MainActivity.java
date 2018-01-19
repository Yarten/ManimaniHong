package com.example.rangebar;

import android.content.res.ColorStateList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RangeBarEx rangeBarEx;
    private RangeBar rangeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rangeBarEx = findViewById(R.id.rangebar_ex);
        rangeBar = findViewById(R.id.rangeBar1);
        rangeBarEx.setRange(-1.0f,1.0f);
        rangeBar.setTickStart(-100f);
        rangeBar.setTickEnd(100f);
        rangeBar.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String value) {
                return String.format("%.2f", Float.parseFloat(value));
            }
        });
        rangeBarEx.setOnRangeBarChangeListener(new RangeBarEx.OnRangeBarChangeListener() {
            @Override
            public void onRangeBarChange(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                float left = Float.parseFloat(leftPinValue);
                float right = Float.parseFloat(rightPinValue);

                Log.i("Rangebar", left + " " + right);
            }
        });
    }
}
