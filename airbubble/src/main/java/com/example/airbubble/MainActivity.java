package com.example.airbubble;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private AirBubble airBubble,airBubble2,airBubble3,airBubble4;
    private Button textView;
    private BubbleToast bubbleToast,myToast;
    private WindowManager windowManager;
    private int width,height;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        context = this;
//        Toast.makeText(this,"asfsfas",Toast.LENGTH_LONG).show();
//        windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(outMetrics);
//        width = outMetrics.widthPixels;
//        height = outMetrics.heightPixels;
//
//        textView = findViewById(R.id.mText);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bubbleToast = new BubbleToast(context,width/3,height/4,"sfassdf");
//                bubbleToast.show();
//                myToast = new BubbleToast(context,width/10,height/2,"asfsa",Gravity.TOP,(float)0.1);
//                myToast.show();
//            }
//        });
    }
}
