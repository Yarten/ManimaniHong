package com.yarten.device.Sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Gravity;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by lenovo on 2018/1/19.
 */

public class Sensor implements SensorEventListener
{

    private SensorManager sensorManager;

    public interface Listener {
        void onChange(float ... values);
    }

    public enum Type
    {
        Accelerometer,
        MagneticField,
        Orientation,
        Light,
        Pressure,
        Temperature,
        Proximity,
        Gravity,
        LinearAcceleration,
        RotationVector
    }

    private SensorManager sm;
    private android.hardware.Sensor sensor;
    private Type type;

    public Sensor(Context context, Type type)
    {
        sm = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(type.ordinal() + 1);
        this.type = type;
    }

    public void turnOn() {
        sm.registerListener(this, sensor, 30000);
    }

    public void turnOff() {
        sm.unregisterListener(this);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        listener.onChange(event.values);
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

    }
}
