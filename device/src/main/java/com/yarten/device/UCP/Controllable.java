package com.yarten.device.UCP;

import java.util.List;
import java.util.Vector;

/**
 * Created by yfic on 2017/12/31.
 */

public interface Controllable
{
    List<Controller> getControllers();

    void setControllers(List<Controller> controllers);

    enum Type
    {
        Button
    }

    Type getType();

    interface Listener
    {
        void onDown();

        void onUp();

        void onMove(Vector<Float> values);
    }

    void setListener(Listener listener);


}
