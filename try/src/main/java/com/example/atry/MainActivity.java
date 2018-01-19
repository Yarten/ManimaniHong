package com.example.atry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yarten.device.Sensor.Sensor;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Sensor sensor;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        sensor = new Sensor(MainActivity.this, Sensor.Type.Orientation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == true) {
                    flag = false;
                    sensor.turnOn();
                }
                else {
                    flag = true;
                    sensor.turnOff();
                }
            }
        });

        sensor.setListener(new Sensor.Listener() {
            @Override
            public void onChange(float... values) {
                String string = "";
                for (int i = 0;i < values.length;i ++)
                    string += values[i] + " ";
                Log.i("Sensor",string);
            }
        });
    }
}
