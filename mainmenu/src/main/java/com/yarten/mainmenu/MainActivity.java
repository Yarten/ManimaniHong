package com.yarten.mainmenu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;

import com.xiaoshq.database.DataOperation;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Host;
import com.yarten.ucp.Manager;
import com.yarten.ucp.Signal;
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
        if(DataOperation.instance == null || DataOperation.instance.getLoginID() == DataOperation.ADMIN_ID)
            initTriangle(buttonPanel.getRightButton(), LoginActivity.class, Gravity.LEFT, Gravity.RIGHT, R.mipmap.users);
        else initTriangle(buttonPanel.getRightButton(), LoginActivity.class, Gravity.LEFT, Gravity.RIGHT, R.mipmap.logout);

        initTriangle(buttonPanel.getLeftButton(), AboutActivity.class, Gravity.RIGHT, Gravity.LEFT, R.mipmap.member);
        initTriangle(buttonPanel.getTopButton(), ReposActivity.class, Gravity.BOTTOM, Gravity.TOP, R.mipmap.cloud);
        initTriangle(buttonPanel.getBottomButton(), DeviceActivity.class, Gravity.TOP, Gravity.BOTTOM, R.mipmap.wifi);

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
                        else // 进入第二级界面时，跳转到新增编辑界面
                        {
                            ActivityHelper.toEditor(MainActivity.this);
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
        new Manager(this.getApplicationContext())
                .setSelfName("Honor V9");
        new DataOperation(this);
    }

    private void initNotFirstTime(Bundle bundle)
    {
        buttonPanel.toggle(true);
        int direction = bundle.getInt("Direction", -1);
        if(direction != -1)
            ActivityHelper.setSlideIn(this, direction);
    }

    private void initTriangle(ShapeButton button, Class target, int inDirection, int outDirection, int imageID)
    {
        ActivityHelper.initTriangle(button, buttonPanel, this, target, inDirection, outDirection, imageID);
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
