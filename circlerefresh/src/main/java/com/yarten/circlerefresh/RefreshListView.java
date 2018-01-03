package com.yarten.circlerefresh;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.yarten.utils.CommonRecyclerView;

import java.util.List;

/**
 * Created by yfic on 2018/1/1.
 */

public class RefreshListView<T, E extends CommonRecyclerView.ViewHolder<T>> extends ConstraintLayout
{
    private Context context;
    private CommonRecyclerView commonRecyclerView;
    private CommonRecyclerView.Adapter<T, E> adapter;
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

    public RefreshListView setAdapter(CommonRecyclerView.Adapter adapter)
    {
        commonRecyclerView.setAdapter(adapter);
        this.adapter = adapter;
        return this;
    }

    public RefreshListView setAdapter(int layout, Class<E> holderClass)
    {
        return setAdapter(new CommonRecyclerView.Adapter(context, layout, holderClass));
    }

    public void add(T data, int position)
    {
        if(adapter != null) adapter.add(data, position);
    }

    public void remove(int position)
    {
        if(adapter != null) adapter.remove(position);
    }

    public void update(T data, int position)
    {
        if(adapter != null) adapter.update(data, position);
    }

    public void updateAll(List<T> data)
    {
        if(adapter != null) adapter.updateAll(data);
    }

    public void finishRefreshing()
    {
        circleRefreshLayout.finishRefreshing();
    }

    public CommonRecyclerView getCommonRecyclerView(){return commonRecyclerView;}
}
