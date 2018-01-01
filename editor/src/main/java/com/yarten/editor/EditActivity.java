package com.yarten.editor;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yarten.utils.Interface.Styleable.Color;

import com.yarten.circlepicker.CirclePickerView;
import com.yarten.utils.Interface.Styleable;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
    }

    private TextInputEditText text;
    private CirclePickerView rotation, scale;
    private ColorPicker color;
    private ShapePicker shape;

    private void initView()
    {
        text = findViewById(R.id.text_input);

        rotation = findViewById(R.id.rotation_picker);
        rotation.setOnValueChangeListener(new CirclePickerView.OnValueChangeListener() {
            @Override
            public void onValueChanging(CirclePickerView pickerView, double value) {

            }

            @Override
            public void onValueChanged(CirclePickerView pickerView, double value) {

            }
        });

        scale = findViewById(R.id.scale_picker);
        scale.setOnValueChangeListener(new CirclePickerView.OnValueChangeListener() {
            @Override
            public void onValueChanging(CirclePickerView pickerView, double value) {

            }

            @Override
            public void onValueChanged(CirclePickerView pickerView, double value) {

            }
        });

        shape = findViewById(R.id.shape_picker);
        shape.setListener(new ShapePicker.Listener() {
            @Override
            public void onPick(Styleable.Shape shape) {

            }
        });

        color = findViewById(R.id.color_picker);
        color.setOnPickListener(new ColorPicker.Listener() {
            @Override
            public void onPick(int color) {

            }
        }).build(3, 3,
                Color.Qing, Color.Green, Color.Blue,
                Color.Purple, Color.Dark, Color.Orange,
                Color.Red, Color.White, Color.Gray);
    }
}
