package com.yarten.utils;

import java.util.LinkedList;

/**
 * Created by yfic on 2018/1/18.
 */

public class FiniteLinkedList<E> extends LinkedList<E>
{
    private int queueSize = -1;

    public void setQueueSize(int size)
    {
        queueSize = size;
        cut();
    }

    @Override
    public boolean add(E o)
    {
        boolean result = super.add(o);
        cut();
        return result;
    }

    private void cut()
    {
        while(queueSize >= 0 && queueSize > super.size())
            super.remove(0);
    }
}
