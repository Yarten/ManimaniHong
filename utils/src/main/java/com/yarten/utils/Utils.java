package com.yarten.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.yarten.utils.Interface.EditDialogCallback;

import java.util.List;

/**
 * Created by yfic on 2017/12/26.
 */

public class Utils
{
    private static Handler handler = new Handler();

    public static void makeDialog(Context context, String msg, AlertDialog.OnClickListener listener)
    {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", listener)
                .show();
    }

    public static void makeEditDialog(Context context, String msg, final EditDialogCallback callback)
    {
        final EditText et = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle(msg)
                .setView(et)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onConfirm(et.getText().toString());
                    }
                })
                .show();
    }

    public static void makeToast(final Context context, final String msg)
    {
        if(Thread.currentThread() == Looper.getMainLooper().getThread())
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        else
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
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
