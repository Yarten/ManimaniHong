package com.yarten.jsonconverter;

import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controller;
import com.yarten.utils.Style;
import com.yarten.widget.Widget;

import java.util.List;

/**
 * Created by ivan on 2018/1/19.
 */

public class Component
{
    public List<Controller> controllers;

    public Style style;

    public Widget.Type type;

    public String name, description;
}
