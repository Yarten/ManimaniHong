package com.example.airbubble;

import android.content.Context;
import android.graphics.PixelFormat;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2018/1/10.
 */

public class BubbleToast {
    private static boolean numOfViews = true;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private AirBubble airBubble;
    private Context context;
    private View view;

    public BubbleToast(Context context, float x, float y, String text) {
        airBubble = new AirBubble(context);
        this.context = context;

        airBubble.setX(x);
        airBubble.setY(y);
        airBubble.setText(text);
        airBubble.setTextAlignCenter();
    }

    public BubbleToast(Context context, float x, float y, String text, int Direction, float Percentage) {
        airBubble = new AirBubble(context);
        this.context = context;

        airBubble.setX(x);
        airBubble.setY(y);
        airBubble.setText(text);
        airBubble.setTextAlignCenter();
        airBubble.setBubbleLayout(Direction,Percentage);
    }

    public int getWidth() {
        return airBubble.returnWidth();
    }

    public int getHeight() {
        return airBubble.returnHeight();
    }

    public void setSize(int width, int height) {
        airBubble.setSize(width, height);
    }

    public void setText(String text) {
        airBubble.setText(text);
    }

    public void show() {
        airBubble.setEnabled(false);
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        view = airBubble.getRootView();

        try {
            windowManager.addView(view,params);
        }
        catch (WindowManager.BadTokenException e) {

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(view);
            }
        }, 3000);//3秒后执行Runnable中的run方法
    }
}
