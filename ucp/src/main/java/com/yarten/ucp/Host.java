package com.yarten.ucp;

import android.support.annotation.NonNull;

/**
 * Created by yfic on 2017/12/31.
 */

public class Host implements Comparable<Host>
{
    public String host;
    public String name;
    public String password;
    public int port;

    public Host(String host, int port, String name)
    {
        this.host = host;
        this.port = port;
        this.name = name;
        state = State.Discovered;
        discoveredTime = System.currentTimeMillis();
    }

    long discoveredTime;
    State state;
    int watchDog = 0;

    public enum State
    {
        Discovered, Connected, Connecting
    }

    public State getState(){return state;}

    @Override
    public int compareTo(@NonNull Host o) {
        return name.compareTo(o.name);
    }

    public String toString()
    {
        return host + " " + name;
    }

    public Host clone()
    {
        Host newHost = new Host(host, port, name);
        newHost.state = state;
        newHost.password = password;
        return newHost;
    }
}