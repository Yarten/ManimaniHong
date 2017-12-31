package com.yarten.shapebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yarten.utils.Interface.Touchable;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private ButtonPanel buttonPanel;
    private boolean isReverse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPanel = findViewById(R.id.button_panel);
        buttonPanel.toggle();
        buttonPanel
                .getMiddleButton()
                .setListener(new Touchable.Listener() {
                    @Override
                    public void onDown() {
                        if(!isReverse)
                        {
                            buttonPanel.toggle();
                            isReverse = true;
                        }
                    }

                    @Override
                    public void onUp() {

                    }

                    @Override
                    public void onMove(Vector<Float> values) {

                    }
                });
        buttonPanel
                .getLeftTopButton()
                .setListener(new Touchable.Listener() {
                    @Override
                    public void onDown() {
                        if(isReverse)
                        {
                            buttonPanel.toggle();
                            isReverse = false;
                        }
                    }

                    @Override
                    public void onUp() {

                    }

                    @Override
                    public void onMove(Vector<Float> values) {

                    }
                });
    }
}
