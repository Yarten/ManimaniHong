package com.yarten.device.UCP;

import com.yarten.device.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by yfic on 2017/12/30.
 */

public class Package
{
    //region 构造器
    /**
     * Boolean: 布尔型指令，包含0/1
     * Vector: 向量型指令，包含若干维归一化的-1~1
     * Hello: 探测包（受控端发出，包含其IP和名字）
     * Connect: 连接包（包含请求代码与密码）
     * List: 控制列表（受控端发出，包含可控制的列表项）
     * Reply: 回复包（包含代码）
     */
    public enum Type
    {
        Boolean, Vector, Hello, Connect, List, Reply, Undefined
    }

    public static class IncompatibleType extends Exception
    {
        public IncompatibleType(Type ori, Type used)
        {
            super(String.format("IncompatibleType: should be %s but used %s", ori.toString(), used.toString()));
        }
    }

    private Type type;

    public Package(Type type)
    {
        this.type = type;
    }

    private void check(Type shouldBe) throws IncompatibleType
    {
        if(BuildConfig.DEBUG && shouldBe != type) throw new IncompatibleType(shouldBe, type);
    }

    public Type getType(){return type;}
    //endregion

    //region 包内容
    List<Signal> signals = new ArrayList<>();
    boolean bValue, connected;
    Vector<Float> fValus = new Vector<>();
    String signal, host, name, password, selfName;
    int code;
    //endregion

    //region Getter
    /**
     * 获得包每一组条目的数量
     * @return 每一组条目的数量，而不是模块数量
     */
    public int getLength()
    {
        switch (type)
        {
            case Vector: return fValus.size();
            case List: return signals.size();
            case Undefined: return 0;
            default: return 1;
        }
    }

    public String getSignal()
    {
        return signal;
    }

    public String getHost() throws IncompatibleType
    {
        check(Type.Hello);
        return host;
    }

    public String getName() throws IncompatibleType
    {
        check(Type.Hello);
        return name;
    }

    public int getReply() throws IncompatibleType
    {
        check(Type.Reply);
        return code;
    }

    public boolean isConnected() throws IncompatibleType
    {
        check(Type.Connect);
        return connected;
    }

    public List<Signal> getList() throws IncompatibleType
    {
        check(Type.List);
        return signals;
    }

    public boolean getBoolean() throws IncompatibleType
    {
        check(Type.Boolean);
        return bValue;
    }

    public Vector<Float> getVector() throws IncompatibleType
    {
        check(Type.Vector);
        return fValus;
    }
    //endregion

    //region 包配置
    public Package setSignal(String signal)
    {
        this.signal = signal;
        return this;
    }

    public Package setBoolean(boolean b) throws IncompatibleType
    {
        check(Type.Boolean);
        bValue = b;
        return this;
    }

    public Package setVector(float ... values) throws IncompatibleType
    {
        Vector<Float> vector = new Vector<>();
        for(float value : values)
            vector.add(value);
        return setVector(vector);
    }

    public Package setVector(Vector<Float> vector) throws IncompatibleType
    {
        check(Type.Vector);
        fValus = vector;
        return this;
    }

    public Package setSignals(List<Signal> signals) throws  IncompatibleType
    {
        check(Type.List);
        this.signals = signals;
        return this;
    }

    public Package setConnect(String selfName, boolean wantToConnect) throws IncompatibleType
    {
        return setConnect(selfName, wantToConnect, "%");
    }

    public Package setConnect(String selfName, boolean wantToConnect, String password) throws IncompatibleType
    {
        check(Type.Connect);
        connected = wantToConnect;
        this.password = password;
        this.selfName = selfName;
        return this;
    }

    public Package setReply(int code) throws IncompatibleType
    {
        check(Type.Reply);
        this.code = code;
        return this;
    }

    public Package setHello(String host, String name) throws IncompatibleType
    {
        check(Type.Hello);
        this.host = host;
        this.name = name;
        return this;
    }
    //endregion



}