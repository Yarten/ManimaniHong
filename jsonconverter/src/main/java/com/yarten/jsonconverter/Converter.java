package com.yarten.jsonconverter;

import android.view.View;

import com.google.gson.Gson;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.widget.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2018/1/4.
 */

public class Converter
{
    //region 从对象转化为JSON字符串
    public static String toJSON(Styleable styleable)
    {
        StringBuilder builder = new StringBuilder("{\"style\": {");
        builder.append(toJSON(styleable.getStyle()));
        builder.append("}}");
        return builder.toString();
    }

    public static String toJSON(Style style)
    {
        Gson gson = new Gson();
        return gson.toJson(style);
    }

    public static String toJSON(Controller controller)
    {
        Gson gson = new Gson();
        return gson.toJson(controller);
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
            if (i < list.size()-1)
                stringBuilder.append(",");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    public static String toJSON(List<View> views, List<Widget> widgets)
    {
        List<Component> components = new ArrayList<>();

        for(int i = 0, size = views.size(); i <size; i++)
        {
            Component component = new Component();
            Widget widget = widgets.get(i);
            component.controllers = ((Controllable)views.get(i)).getControllers();
            component.style = ((Styleable)views.get(i)).getStyle();
            component.name = widget.name;
            component.description = widget.description;
            component.type = widget.type;

            components.add(component);
        }

        Gson gson = new Gson();
        ComponentList componentList = new ComponentList();
        componentList.components = components;
        String json = gson.toJson(componentList);
        return json;

    }
    //endregion

    // region
    // 从JSON字符串转化为对象
    public static Controller toController(String json)
    {
        Gson gson = new Gson();
        Controller controller = gson.fromJson(json, Controller.class);
        return controller;
    }

    // 从JSON字符串转化为List<Component>
    public static void toSolution(String json, List<Component> components)
    {
        Gson gson = new Gson();
        ComponentList componentList = gson.fromJson(json, ComponentList.class);
        components = componentList.components;
    }

    // 从JSON字符串转化为List<Component>
    public static List<Component> toSolution(String json)
    {
        Gson gson = new Gson();
        ComponentList componentList = gson.fromJson(json, ComponentList.class);
        return componentList.components;
    }
    //endregion
}
