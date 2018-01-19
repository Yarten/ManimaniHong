package com.yarten.shapebutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yarten.ucp.Controllable;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yfic on 2018/1/19.
 */

public class ToggleButton extends ShapeButton
{
    public ToggleButton(Context context)
    {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public interface Listener
    {
        void onStateChange(boolean state);
    }

    private boolean state = false;
    private Listener listener = null;

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(!isEnabled()) return super.onTouchEvent(event);

        switch (event.getAction())
        {
            case ACTION_DOWN:
                if(!state)
                {
                    onDown();
                }
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                if(state)
                {
                    onUp();
                    state = false;
                }
                else state = true;

                if(listener != null)
                    listener.onStateChange(state);

                break;
        }

        return true;
    }
}
