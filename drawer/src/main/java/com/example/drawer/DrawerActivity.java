package com.example.drawer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class DrawerActivity extends AppCompatActivity {

    //region 定义变量
    private DrawerLayout mainLayout;
    private LinearLayout drawerLayout;
    private GestureDetector gestureDetector;
    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ini();
        gesture();
    }

    //region 关闭抽屉
    public void closeDrawer()
    {
        mainLayout.closeDrawer(Gravity.LEFT);
    }
    //endregion

    //region 设置监听器
    public void addDrawerListener(DrawerLayout.DrawerListener drawerListener)
    {
        mainLayout.addDrawerListener(drawerListener);
    }
    //endregion

    //region 设置是否使用手势识别
    public void lockDrawer(boolean isLock)
    {
        if(isLock)
        {
            mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
        else
        {
            mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
    //endregion

    //region 绑定控件
    private void ini() {
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout = findViewById(R.id.drawLayout);
    }
    //endregion

    //region 添加layout
    public void setContentView(int mainLayoutID, int drawerLayoutID) {
        LinearLayout main = findViewById(R.id.mainContainer);
        LayoutInflater.from(this).inflate(mainLayoutID, main);
        LayoutInflater.from(this).inflate(drawerLayoutID, drawerLayout);
    }
    //endregion

    //region 手势识别
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            int result=drawTouch(e1.getX(),e1.getY(),e2.getX(),e2.getY());
            if (result == Gravity.RIGHT)
                mainLayout.openDrawer(Gravity.START);   //打开抽屉
            if (result == Gravity.LEFT)
                mainLayout.closeDrawer(Gravity.START);  //关闭抽屉
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private int drawTouch(float x,float y,float upx,float upy){
            /*
             * 如果手指松开时x方向上往右偏移了100px以上，则判断为向右滑动
             * 如果手指松开时x方向上往左偏移了100px以上，则判断为向左滑动
             * 如果手指松开时y方向上向下偏移了100px以上，则判断为向下滑动
             * 如果手指松开时y方向上向上偏移了100px以上，则判断为向上滑动
             * 如果以上的情况都不是，则判断为无滑动。
             */
            if(upx-x>100){
                return Gravity.RIGHT;
            }else if(x-upx>100){
                return Gravity.LEFT;
            }else if(upy-y>100){
                return Gravity.BOTTOM;
            }else if(y-upy>100){
                return Gravity.TOP;
            }
            return Gravity.CENTER;
        }
    }
    //endregion

    //region 实例
    private void gesture() {
        findViewById(R.id.mainLayout)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                });
        gestureDetector = new GestureDetector(DrawerActivity.this,new MyGestureListener());
    }
    //endregion
}
