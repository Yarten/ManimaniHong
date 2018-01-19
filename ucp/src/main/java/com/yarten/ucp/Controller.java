package com.yarten.ucp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.yarten.ucp.Controllable.Type;

/**
 * Created by yfic on 2017/12/31.
 */

public class Controller
{
    private final Type type;
    private final String name;

    public Controller(Type type, String name)
    {
        this.type = type;
        this.name = name;
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

    public static class Pair
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

    public String getName(){return name;}
}
