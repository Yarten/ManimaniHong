package com.yarten.sensorswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.yarten.device.Sensor.Sensor;
import com.yarten.shapebutton.ToggleButton;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;

import java.util.List;
import java.util.Vector;

/**
 * Created by yfic on 2018/1/19.
 */

public class SensorSwitch extends ToggleButton implements Controllable
{
    public SensorSwitch(Context context)
    {
        this(context, Sensor.Type.Undefined);
    }

    public SensorSwitch(Context context, Sensor.Type type)
    {
        this(context, null, type);
    }

    public SensorSwitch(Context context, AttributeSet attrs)
    {
        this(context, attrs, Sensor.Type.Undefined);
    }

    public SensorSwitch(Context context, AttributeSet attrs, Sensor.Type type)
    {
        super(context, attrs);
        if(type == Sensor.Type.Undefined)
        {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SensorSwitch);
            type = Sensor.Type.values()[ta.getInt(R.styleable.SensorSwitch_SensorType, Sensor.Type.Orientation.ordinal())];
            ta.recycle();
        }

        sensor = new Sensor(context, type);
        initImage(type);
        initEvent();
    }

    private Sensor sensor;

    private void initImage(Sensor.Type type)
    {
        switch (type)
        {
            case Orientation:
                super.setImage(R.drawable.orientation);
                return;
            case Accelerometer:
                super.setImage(R.drawable.accelerometer);
                return;
        }
    }

    private void initEvent()
    {
        super.setListener(new ToggleButton.Listener() {
            @Override
            public void onStateChange(boolean state) {
                if(state)
                    sensor.turnOn();
                else sensor.turnOff();
            }
        });

        sensor.setListener(new Sensor.Listener() {
            @Override
            public void onChange(float... values)
            {
                if(listener == null) return;

                Vector<Float> v = new Vector<>();
                for(int i = 0; i < values.length; i++)
                    v.add(values[i]);

                listener.onMove(v);
            }
        });
    }

    //region Controller设置
    private List<Controller> controllers;

    @Override
    public List<Controller> getControllers() {
        return controllers;
    }

    private Controllable.Listener listener = null;

    @Override
    public void setListener(Controllable.Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void setControllers(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public Type getType() {
        return Type.Boolean;
    }
    //endregion
}
