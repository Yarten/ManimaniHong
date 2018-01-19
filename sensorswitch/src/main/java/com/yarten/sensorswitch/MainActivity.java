package com.yarten.sensorswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yarten.ucp.Controllable;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorSwitch s1 = findViewById(R.id.sensorSwitch);
        s1.setListener(new Controllable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }

            @Override
            public void onMove(Vector<Float> values) {
                Log.i("Orientation", values.toString());
            }
        });

        SensorSwitch s2 = findViewById(R.id.sensorSwitch2);
        s2.setListener(new Controllable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }

            @Override
            public void onMove(Vector<Float> values) {
                Log.i("Gravity", values.toString());
            }
        });

        SensorSwitch s3 = findViewById(R.id.sensorSwitch3);
        s3.setListener(new Controllable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }

            @Override
            public void onMove(Vector<Float> values) {
                Log.i("Accelerator", values.toString());
            }
        });
    }
}
