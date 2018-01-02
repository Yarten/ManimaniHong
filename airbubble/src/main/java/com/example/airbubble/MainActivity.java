package com.example.airbubble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AirBubble airBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        airBubble = findViewById(R.id.MyBubble);
        airBubble.setText("This is my bubble.This is my bubble.This is my bubble.This is my bubble.This is my bubble.");
    }
}
