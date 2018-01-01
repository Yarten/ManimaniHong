package com.yarten.circlerefresh;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.yarten.utils.CommonRecyclerView;

/**
 * Created by yfic on 2018/1/1.
 */

public class RefreshListView extends ConstraintLayout
{
    private Context context;
    private CommonRecyclerView commonRecyclerView;
    private CommonRecyclerView.Adapter adapter;
    private CircleRefreshLayout circleRefreshLayout;

    public RefreshListView(Context context)
    {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.circle_refresh, this);

        this.context = context;
        commonRecyclerView = findViewById(R.id.common_list);
        circleRefreshLayout = findViewById(R.id.circle_refresh_layout);
    }


    public RefreshListView setOnCircleRefreshListener(CircleRefreshLayout.OnCircleRefreshListener onRefreshListener)
    {
        circleRefreshLayout.setOnRefreshListener(onRefreshListener);
        return this;
    }

    public RefreshListView setAdapter(int layout, Class holderClass)
    {
        adapter = new CommonRecyclerView.Adapter(context, layout, holderClass);
        commonRecyclerView.setAdapter(adapter);
        return this;
    }

    public void add(int position)
    {
        if(adapter != null) adapter.add(position);
    }

    public void remove(int position)
    {
        if(adapter != null) adapter.remove(position);
    }

    public void update(int position)
    {
        if(adapter != null) adapter.update(position);
    }

    public void updateAll(int size)
    {
        if(adapter != null) adapter.updateAll(size);
    }

    public void finishRefreshing()
    {
        circleRefreshLayout.finishRefreshing();
    }
}
