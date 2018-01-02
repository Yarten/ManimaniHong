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

    public static class Value {
        public float min;
        public float max;
        public float num;
        public Value(float min, float max, float num) {
            this.min = min;
            this.max = max;
            this.num = num;
        }
    }

    public class Pair
    {
        public Signal signal;
        public Value value;

        public Pair(Signal signal, Value value)
        {
            this.signal = signal;
            this.value = value;
        }
    }

    private List<Pair> controlList = new LinkedList<>();

    public void add(Signal signal, Value value)
    {
        controlList.add(new Pair(signal, value));
    }

    public void remove(int i)
    {
        controlList.remove(i);
    }

    public List<Pair> getControlList(){return controlList;}

    public Type getType(){return type;}
}
