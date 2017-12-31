package com.yarten.device.UCP;

import com.yarten.device.UCP.Signal.Type;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yfic on 2017/12/31.
 */

public class Controller
{
    private final Type type;

    public Controller(Type type)
    {
        this.type = type;
    }

    public static class Range
    {
        public float min, max;

        public Range(float min, float  max)
        {
            this.min = min;
            this.max = max;
        }
    }

    public class Pair
    {
        public Signal signal;
        public Object value;

        public Pair(Signal signal, Object value)
        {
            this.signal = signal;
            this.value = value;
        }
    }

    private List<Pair> controlList = new LinkedList<>();

    public void add(Signal signal, Object value)
    {
        controlList.add(new Pair(signal, value));
    }

    public void remove(int i)
    {
        controlList.remove(i);
    }

    public List<Pair> getControlList(){return controlList;}
}
