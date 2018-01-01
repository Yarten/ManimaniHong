package com.yarten.circlerefresh;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yarten.utils.CommonRecyclerView;

public class Main2Activity extends AppCompatActivity {

    RefreshListView refreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        refreshListView = findViewById(R.id.refresh_list_view);
        refreshListView.setOnCircleRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {

            }

            @Override
            public void refreshing() {
                Log.i("Refresh", " .. . . . . . ");
            }
        }).setAdapter(R.layout.item_layout, A.class).updateAll(10);
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
