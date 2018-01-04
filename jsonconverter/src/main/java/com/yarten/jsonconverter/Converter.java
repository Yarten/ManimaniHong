package com.yarten.jsonconverter;

import com.google.gson.Gson;
import com.yarten.device.UCP.Controllable;
import com.yarten.device.UCP.Controller;
import com.yarten.utils.Style;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by yfic on 2018/1/4.
 */

public class Converter
{
    //region 从对象转化为JSON字符串
    public static String toJSON(Style style)
    {
        Gson gson = new Gson();
        String json = gson.toJson(style);
        return json;
    }

    public static String toJSON(Controller controller)
    {
        Gson gson = new Gson();
        String json = gson.toJson(controller);
        return json;
    }

    public static String toJSON(Controllable controllable)
    {
        // TODO: 还需要做：类型Type

        List<Controller> list = controllable.getControllers();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"controller\": [");
        for (int i=0; i<list.size(); i++) {
            String temp = toJSON(list.get(i));
            stringBuilder.append(temp);
            if (i < list.size()-1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
    //endregion

    //region 从JSON字符串转化为对象
    public Controller toController(String json)
    {
        Gson gson = new Gson();
        Controller controller = gson.fromJson(json, Controller.class);
        return controller;
    }
    //endregion
}
