package com.yarten.utils.Interface;

import java.util.Vector;

/**
 * Created by yfic on 2017/12/26.
 */

public interface Basic
{
    interface Listener
    {
        void onDown();

        void onUp();

        void onMove(Vector<Float> values);
    }



    void setListener(Listener listener);
}
