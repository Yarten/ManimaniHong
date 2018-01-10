package com.yarten.rocker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yarten.ucp.Controllable;
import com.yarten.utils.Interface.Styleable;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rocker rocker = findViewById(R.id.rocker);
        rocker.setColor(Styleable.Color.Qing);
        rocker.setListener(new Controllable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }

            @Override
            public void onMove(Vector<Float> values) {
                Log.i("Rocker", values.get(0) + " " + values.get(1));
            }
        });
    }
}
