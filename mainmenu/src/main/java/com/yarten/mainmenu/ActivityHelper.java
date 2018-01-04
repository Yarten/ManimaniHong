package com.yarten.mainmenu;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Window;

import com.yarten.device.UCP.Controllable;
import com.yarten.editor.*;
import com.yarten.shapebutton.ButtonPanel;
import com.yarten.shapebutton.ShapeButton;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Created by yfic on 2017/12/27.
 */

class ActivityHelper
{
    //region Activity栈管理
    private static List<BaseActivity> activities = new LinkedList<>();

    public static void add(BaseActivity activity)
    {
        activities.add(activity);
        while(activities.size() >= 3)
        {
            BaseActivity lastOne = activities.get(0);
            lastOne.finish();
            activities.remove(0);
        }
    }

    public static void pop()
    {
        int size = activities.size();
        if(size != 0)
            activities.remove(size-1);
    }

    public static int size()
    {
        return activities.size();
    }

    public static void exit()
    {
        for(BaseActivity activity : activities)
        {
            activity.finish();
        }

        System.exit(0);
    }
    //endregion

    //region Activity切换动画
    static void setSlideOut(AppCompatActivity activity, int direction)
    {
        Window window = activity.getWindow();
        window.setExitTransition(new Slide(direction));
    }

    static void setSlideIn(AppCompatActivity activity, int direction)
    {
        Window window = activity.getWindow();
        window.setEnterTransition(new Slide(direction));
    }

    static void setExplode(AppCompatActivity activity)
    {
        Window window = activity.getWindow();
        window.setExitTransition(new Explode());
    }

    static void setTransition(AppCompatActivity activity)
    {
        Window window = activity.getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setEnterTransition(new Fade(Visibility.MODE_IN));
        window.setExitTransition(new Fade(Visibility.MODE_OUT));
    }

    static void setTransition(AppCompatActivity activity, int inDirection, int outDirection)
    {
        Window window = activity.getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setEnterTransition(new Slide(inDirection));
        window.setExitTransition(new Slide(outDirection));
    }
    //endregion

    //region 控件设置
    static void initTriangle(ShapeButton button, final ButtonPanel buttonPanel, final AppCompatActivity source, final Class target, final int inDirection, final int outDirection)
    {
        button.setListener(new Controllable.Listener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {
                Intent intent = new Intent(source, target);
                Bundle bundle = new Bundle();
                bundle.putInt("Direction", inDirection);
                intent.putExtras(bundle);

                setSlideOut(source, outDirection);
                source.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(
                                source,
                                buttonPanel, ID2S(source, R.string.button_panel)
                        ).toBundle());
            }

            @Override
            public void onMove(Vector<Float> values) {

            }
        });
    }
    //endregion

    //region Activity跳转 -》 编辑界面/使用界面
    public static void toEditor(AppCompatActivity src)
    {
        Intent intent = new Intent(src, com.yarten.editor.MainActivity.class);
        src.startActivity(intent);
    }
    //endregion

    static String ID2S(Context context, int id)
    {
        return context.getResources().getString(id);
    }
}
