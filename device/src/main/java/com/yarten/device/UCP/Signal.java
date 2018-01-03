package com.yarten.device.UCP;

/**
 * Created by yfic on 2017/12/30.
 */

public class Signal
{
    public enum Type
    {
        Boolean, Vector, Undefined
    }

    String name;
    String description;
    Type type;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Signal(String name, Type type, String description)
    {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public String toString()
    {
        return name + " " + type.toString() + " " + description;
    }
}
