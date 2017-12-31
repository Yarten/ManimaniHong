package com.yarten.editor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPicker colorPicker = findViewById(R.id.color_picker);
        colorPicker.setOnPickListener(new ColorPicker.Listener() {
            @Override
            public void onPick(int color) {
                Log.i("Color Picker", color + "");
            }
        }).build(3, 2, Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN);
    }
}
