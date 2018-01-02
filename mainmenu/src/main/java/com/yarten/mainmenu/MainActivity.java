package com.yarten.mainmenu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;

import com.yarten.device.UCP.Controllable;
import com.yarten.shapebutton.ButtonPanel;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Utils;

import java.util.Vector;

public class MainActivity extends BaseActivity {

    private ButtonPanel buttonPanel;
    private boolean isSubMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHelper.setTransition(this);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        init();
        if(bundle == null)
            initFirstTime();
        else initNotFirstTime(bundle);
    }

    private void init()
    {
        buttonPanel = findViewById(R.id.button_panel);
        initTriangle(buttonPanel.getRightButton(), LoginActivity.class, Gravity.LEFT, Gravity.RIGHT);
        initTriangle(buttonPanel.getLeftButton(), AboutActivity.class, Gravity.RIGHT, Gravity.LEFT);
        initTriangle(buttonPanel.getTopButton(), ReposActivity.class, Gravity.BOTTOM, Gravity.TOP);
        initTriangle(buttonPanel.getBottomButton(), DeviceActivity.class, Gravity.TOP, Gravity.BOTTOM);

        buttonPanel.getMiddleButton()
                .setListener(new Controllable.Listener() {
                    @Override
                    public void onDown() {}

                    @Override
                    public void onUp() {
                        if(!isSubMenu)
                        {
                            buttonPanel.toggle();
                            isSubMenu = true;
                        }
                    }

                    @Override
                    public void onMove(Vector<Float> values) {}
                });

        buttonPanel.getLeftTopButton()
                .setListener(new Controllable.Listener() {
                    @Override
                    public void onDown() {}

                    @Override
                    public void onUp() {
                        if(isSubMenu)
                        {
                            buttonPanel.toggle();
                            isSubMenu = false;
                        }
                    }

                    @Override
                    public void onMove(Vector<Float> values) {}
                });
    }

    private void initFirstTime()
    {
        buttonPanel.toggle();
    }

    private void initNotFirstTime(Bundle bundle)
    {
        buttonPanel.toggle(true);
        int direction = bundle.getInt("Direction", -1);
        if(direction != -1)
            ActivityHelper.setSlideIn(this, direction);
    }

    private void initTriangle(ShapeButton button, Class target, int inDirection, int outDirection)
    {
        ActivityHelper.initTriangle(button, buttonPanel, this, target, inDirection, outDirection);
    }

    @Override
    public void onBackPressed()
    {
        Utils.makeDialog(this, "确定要退出应用吗", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ActivityHelper.exit();
            }
        });
    }
}
