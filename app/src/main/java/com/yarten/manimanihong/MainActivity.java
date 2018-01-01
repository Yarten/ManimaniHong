package com.yarten.manimanihong;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yarten.utils.CommonRecyclerView;

public class MainActivity extends AppCompatActivity {

    private CommonRecyclerView recyclerView;
    private CommonRecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.common_list);
        adapter = new CommonRecyclerView.Adapter(this, R.layout.item_layout, 10,
                new CommonRecyclerView.Adapter.Listener() {
            private Button button;
            private TextView textView;

            @Override
            public void onBind(View view, final int position) {
                button = view.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                textView = view.findViewById(R.id.textView);
                textView.setText(position + "");
            }

            @Override
            public void onClick(View view, int position) {
                Log.i("Test", position + "");
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.i("Test", position + " aaa");
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
