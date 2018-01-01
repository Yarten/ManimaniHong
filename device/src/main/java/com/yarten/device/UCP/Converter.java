package com.yarten.device.UCP;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by yfic on 2017/12/30.
 */

public class Converter
{
    public String toString(Package pkg)
    {
        Package.Type type = pkg.getType();
        StringBuilder builder = new StringBuilder(type.toString());

        try
        {
            switch (type)
            {
                case Reply: reply2s(builder, pkg); break;
                case Hello: hello2s(builder, pkg); break;
                case List: list2s(builder, pkg); break;
                case Connect: connect2s(builder, pkg); break;
                case Boolean: boolean2s(builder, pkg); break;
                case Vector: vector2s(builder, pkg); break;
                default: throw new Exception("Unknow Type of Package.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public String toString(List<Package> pkgs)
    {
        StringBuilder builder = new StringBuilder();

        for(int i = 0, size = pkgs.size(); i < size; i++)
        {
            builder.append('@');
            builder.append(toString(pkgs.get(i)));
        }

        if(builder.length() != 0) builder.deleteCharAt(0);
        return builder.toString();
    }

    //region 将各种包转换为字符串
    private void reply2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        builder.append('#');
        builder.append(pkg.code);
    }

    private void hello2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        builder.append('#');
        builder.append(pkg.host);
        builder.append('#');
        builder.append(pkg.name);
    }

    private void list2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        for(Signal signal : pkg.signals)
        {
            builder.append('#');
            builder.append(signal.name);
            builder.append('#');
            builder.append(signal.type.toString());
            builder.append('#');
            builder.append(signal.description);
        }
    }

    private void connect2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        builder.append('#');
        builder.append(pkg.selfName);
        builder.append('#');
        builder.append(pkg.connected ? '1' : '0');
        builder.append('#');
        builder.append(pkg.password);
    }

    private void boolean2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        builder.append('#');
        builder.append(pkg.signal);
        builder.append('#');
        builder.append(pkg.bValue ? '1':'0');
    }

    private void vector2s(StringBuilder builder, Package pkg) throws Package.IncompatibleType
    {
        builder.append('#');
        builder.append(pkg.signal);
        for(float value : pkg.fValus)
        {
            builder.append('#');
            builder.append(value);
        }
    }
    //endregion

    public Package toPackage(String s) throws Package.IncompatibleType
    {
        String[] words = s.split("#");
        Package.Type type = getType(words[0]);
        Package pkg = new Package(type);

        try
        {
            switch (type)
            {
                case Reply: s2reply(pkg, words); break;
                case Hello: s2hello(pkg, words); break;
                case List: s2list(pkg, words); break;
                case Connect: s2connect(pkg, words); break;
                case Boolean: s2boolean(pkg, words); break;
                case Vector: s2vector(pkg, words); break;
                default: throw new Exception("Unknow Type of Package.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return pkg;
    }

    //region 字符串解析
    private Package.Type getType(String word)
    {
        switch (word)
        {
            case "Boolean": return Package.Type.Boolean;
            case "Vector": return Package.Type.Vector;
            case "Hello": return Package.Type.Hello;
            case "Connect": return Package.Type.Connect;
            case "List": return Package.Type.List;
            case "Reply": return Package.Type.Reply;
            default: return Package.Type.Undefined;
        }
    }

    private void s2reply(Package pkg, String[] words) throws Package.IncompatibleType
    {
        pkg.setReply(Integer.parseInt(words[1]));
    }

    private void s2hello(Package pkg, String[] words) throws Package.IncompatibleType
    {
        pkg.setHello(words[1], words[2]);
    }

    private void s2list(Package pkg, String[] words) throws Package.IncompatibleType
    {
        List<Signal> signals = new ArrayList<>();
        for(int i = 1, size = words.length; i < size; i += 3)
        {
            Signal.Type type = null;
            switch (words[i+1])
            {
                case "Boolean": type = Signal.Type.Boolean; break;
                case "Vector": type = Signal.Type.Vector; break;
            }

            signals.add(new Signal(words[i], type, words[i+2]));
        }
        pkg.setSignals(signals);
    }

    private void s2connect(Package pkg, String[] words) throws Package.IncompatibleType
    {
        pkg.setConnect(words[1], words[2].equals("1"), words[3]);
    }

    private void s2boolean(Package pkg, String[] words) throws Package.IncompatibleType
    {
        pkg.setSignal(words[1]);
        pkg.setBoolean(words[2].equals("1"));
    }

    private void s2vector(Package pkg, String[] words) throws Package.IncompatibleType
    {
        pkg.setSignal(words[1]);
        Vector<Float> values = new Vector<>();
        for(int i = 2, size = words.length; i < size; i++)
            values.add(Float.parseFloat(words[i]));
        pkg.setVector(values);
    }
    //endregion

    // 将Controller对象转换为JSON
    public String toString(Controller controller) {
        Gson gson = new Gson();
        String json = gson.toJson(controller);
        return json;
    }

    // 将JSON转换为Controller对象
    public Controller toController(String json) {
        Gson gson = new Gson();
        Controller controller = gson.fromJson(json, Controller.class);
        return controller;
    }
}
