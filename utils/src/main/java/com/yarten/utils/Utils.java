package com.yarten.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Window;

/**
 * Created by yfic on 2017/12/26.
 */

public class Utils
{
    public static void makeDialog(Context context, String msg, AlertDialog.OnClickListener listener)
    {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", listener)
                .show();
    }

    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue * 1.0f / scale;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void setTransition(AppCompatActivity activity, int inDirection, int outDirection)
    {
        Window window = activity.getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setEnterTransition(new Slide(inDirection));
        window.setExitTransition(new Slide(outDirection));
    }
}
