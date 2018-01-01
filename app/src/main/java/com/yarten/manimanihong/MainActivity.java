package com.yarten.manimanihong;


import android.content.Context;
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
        adapter = new CommonRecyclerView.Adapter(this, R.layout.item_layout, 10, A.class);
        recyclerView.setAdapter(adapter);
    }

    static class A extends CommonRecyclerView.ViewHolder
    {
        public A(Context context, View view)
        {
            super(context, view);

            button = findViewById(R.id.button);
            textView = findViewById(R.id.textView);
        }

        private Button button;
        private TextView textView;

        @Override
        public void onBind(final int position) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                }
            });
            textView.setText(position + "");
        }

        @Override
        public void onClick(int position) {

        }

        @Override
        public boolean onLongClick(int position) {
            return false;
        }
    }
}
