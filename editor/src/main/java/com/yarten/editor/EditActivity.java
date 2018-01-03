package com.yarten.editor;

import android.graphics.Point;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.yarten.utils.Interface.Styleable.Color;

import com.yarten.circlepicker.CirclePickerView;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.utils.Utils;

public class EditActivity extends AppCompatActivity {

    private Styleable currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTransition(this, Gravity.LEFT, Gravity.LEFT);
        setContentView(R.layout.activity_edit);
        initView();
    }

    private void initStyleable()
    {
        View view = WidgetManager.createCurrentView(this);
        ViewGroup viewGroup = findViewById(R.id.widget_layout);
        viewGroup.addView(view);

        WindowManager wm = getWindowManager();
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        View editPanel = findViewById(R.id.edit_panel);

        view.setX((size.x - editPanel.getWidth()) * 0.5f - params.height * 0.5f);
        view.setY(size.y * 0.5f - params.width * 0.5f);

        currentView = (Styleable)view;
    }

    private TextInputEditText text;
    private CirclePickerView rotation, scale;
    private ColorPicker color;
    private ShapePicker shape;

    private void initView()
    {
        Style style = WidgetManager.getCurrentWidget().style;

        text = findViewById(R.id.text_input);
        findViewById(R.id.text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView.setText(text.getText().toString());
                text.clearFocus();
            }
        });

        rotation = findViewById(R.id.rotation_picker);
        rotation.setValue(style.rotation);
        rotation.setOnValueChangeListener(new CirclePickerView.OnValueChangeListener() {
            @Override
            public void onValueChanging(CirclePickerView pickerView, double value) {
                currentView.setBackgroundRotation((int) value);
            }

            @Override
            public void onValueChanged(CirclePickerView pickerView, double value) {
                currentView.setBackgroundRotation((int) value);
            }
        });

        scale = findViewById(R.id.scale_picker);
        scale.setValue(style.scale);
        scale.setOnValueChangeListener(new CirclePickerView.OnValueChangeListener() {
            @Override
            public void onValueChanging(CirclePickerView pickerView, double value) {
                currentView.setScale((float)value);
            }

            @Override
            public void onValueChanged(CirclePickerView pickerView, double value) {
                currentView.setScale((float)value);
            }
        });

        shape = findViewById(R.id.shape_picker);
        shape.setListener(new ShapePicker.Listener() {
            @Override
            public void onPick(Styleable.Shape shape) {
                currentView.setShape(shape);
            }
        });

        int[] colors = Color.colors;
        color = findViewById(R.id.color_picker);
        color.setOnPickListener(new ColorPicker.Listener() {
            @Override
            public void onPick(int color) {
                currentView.setColor(color);
            }
        }).build(3, 3, colors);
        for(int i = 0; i < colors.length; i++)
            if(colors[i] == style.color)
            {
                color.check(i);
                break;
            }

        TextView back = findViewById(R.id.back_button);
        back.setText("<< BACK");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        WidgetManager.updateStyle(currentView);
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        initStyleable();
    }
}
