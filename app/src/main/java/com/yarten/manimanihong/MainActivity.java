package com.yarten.manimanihong;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoshq.database.DataOperation;
import com.xiaoshq.database.Solution;
import com.yarten.jsonconverter.Component;
import com.yarten.jsonconverter.Converter;
import com.yarten.rocker.Rocker;
import com.yarten.ucp.Controllable;
import com.yarten.ucp.Controllable.Type;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yarten.ucp.Controller;
import com.yarten.ucp.Manager;
import com.yarten.ucp.Signal;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ViewGroup viewGroup;
    private Solution solution;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int id = intent.getIntExtra("SolutionID", 1);
        initSolution(id);

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

    private void initSolution(int id)
    {
        DataOperation db = DataOperation.instance;
        solution = db.getSolution(id);

        List<Component> components = Converter.toSolution(solution.detail);

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

        Rocker rocker = new Rocker(this);
        viewGroup.addView(rocker, Utils.dip2px(this, 150), Utils.dip2px(this, 150));
        style.x = 1200;
        style.y = 300;
        style.color = Styleable.Color.Red;
        style.text = "Rocker";
        style.scale = 1.0f;
        style.stylize(rocker);

        rocker.setX(style.x);
        rocker.setY(style.y);

        List<Controller> controllers = new ArrayList<>();
        Controller ctrlX = new Controller(Type.Vector, "X");
        Controller ctrlY = new Controller(Type.Vector, "Y");
        ctrlX.add(new Signal("MX", Type.Vector, "MX"), new Controller.Value(-1, 1, 1));
        ctrlY.add(new Signal("MY", Type.Vector, "MY"), new Controller.Value(-1, 1, 1));
        controllers.add(ctrlX);
        controllers.add(ctrlY);
        rocker.setControllers(controllers);

        Manager.instance.eventBinding(rocker);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
