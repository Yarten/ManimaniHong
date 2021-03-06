package com.yarten.sgbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private SGFloat bt1;
    private SGWidgetButton btWidget;
    TextView t1, t2;
    SGRocker rocker;
    SGFloat btfloat;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);

        btWidget = new SGWidgetButton(findViewById(R.id.button),
                (ViewGroup) findViewById(R.id.ly_main));
        btWidget.setOnAddHandler(new SGWidgetButton.OnAddHandler()
        {
            @Override
            public View createNewView(int userdata)
            {
                SGFloat bt = new SGFloat(new Button(MainActivity.this));
                return bt.getView();
            }
        });
        btWidget.setOnClickListener(new SGWidget.OnActionListener()
        {
            @Override
            public void onAction(View view, MotionEvent event)
            {
                t1.setText(String.format("%f", event.getX()));
                t2.setText(String.format("%f", event.getY()));
            }
        });

        rocker = new SGRocker(MainActivity.this);
        rocker.setX(300);
        rocker.setY(500);
        rocker.setOnRockerListener(new SGRocker.OnRockerListener()
        {
            @Override
            public void onAction(float d, float angle)
            {
            //    t1.setText(String.format("%f", d));
            //    t2.setText(String.format("%f", angle));
            }
        });

        ViewGroup layout = findViewById(R.id.ly_main);
        layout.addView(rocker);

        btfloat = new SGFloat(findViewById(R.id.editText));

    }



}