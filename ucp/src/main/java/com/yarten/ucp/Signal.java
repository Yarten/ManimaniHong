package com.yarten.ucp;

import com.yarten.ucp.Controllable.Type;
/**
 * Created by yfic on 2017/12/30.
 */

public class Signal
{
    public String name;
    public String description;
    public Type type;

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
