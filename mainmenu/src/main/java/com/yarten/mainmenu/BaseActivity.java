package com.yarten.mainmenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.yarten.utils.Utils;

/**
 * Created by yfic on 2017/12/27.
 */

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityHelper.add(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            int direction = bundle.getInt("Direction", -1);
            if(direction != -1)
                ActivityHelper.setTransition(this, direction, direction);
        }
    }

    @Override
    public void onBackPressed()
    {
        ActivityHelper.pop();
        super.onBackPressed();
    }
}
