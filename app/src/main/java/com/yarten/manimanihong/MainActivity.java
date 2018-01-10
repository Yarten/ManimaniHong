package com.yarten.manimanihong;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import com.yarten.ucp.Controllable.Type;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yarten.ucp.Controller;
import com.yarten.ucp.Manager;
import com.yarten.ucp.Signal;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initController();
    }



    private void initView()
    {
        viewGroup = findViewById(R.id.widget_layout);

        FloatingActionButton quit = findViewById(R.id.quit_option);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initController()
    {
        Style style = new Style();
        style.shape = Styleable.Shape.Circle;

        float[] x = {540,220,540,860,1500,2140};
        float[] y = {450,620,780,620,620,620};
        String[] names = {"W", "A", "S", "D", "J", "K"};

        for(int i = 0; i < 6; i++)
        {
            ShapeButton view = new ShapeButton(this);
            viewGroup.addView(view, 200, 200);

            style.x = x[i];
            style.y = y[i];
            style.color = Styleable.Color.colors[i];
            style.text = names[i];
            style.scale = 2;
            style.stylize(view);

            view.setX(x[i]);
            view.setY(y[i]);

            List<Controller> controllers = new ArrayList<>();
            Controller controller = new Controller(Type.Boolean, names[i]);
            controller.add(new Signal(names[i], Type.Boolean, names[i]), new Controller.Value(0, 0, 0));
            controllers.add(controller);
            view.setControllers(controllers);

            Manager.instance.eventBinding(view);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
