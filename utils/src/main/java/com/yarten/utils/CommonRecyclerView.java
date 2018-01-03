package com.yarten.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.w3c.dom.ProcessingInstruction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2017/12/31.
 */

public class CommonRecyclerView extends RecyclerView
{
    private LinearLayoutManager linearLayoutManager;

    public CommonRecyclerView(Context context)
    {
        this(context, null);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setItemAnimator(new DefaultItemAnimator());
        this.linearLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(linearLayoutManager);
    }

    public void setScrollEnable(boolean b)
    {
        linearLayoutManager.canMove = b;
    }

    public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager
    {
        LinearLayoutManager(Context context)
        {
            super(context);
        }

        boolean canMove = true;

        @Override
        public boolean canScrollHorizontally() {
            return canMove && super.canScrollHorizontally();
        }

        @Override
        public boolean canScrollVertically() {
            return canMove && super.canScrollVertically();
        }
    }


    public static class Adapter<T, E extends ViewHolder<T>> extends RecyclerView.Adapter<ViewHolder<T>>
    {
        private List<T> items;
        private int layout;
        private Context context;
        private Class[] parameterTypes;
        private Constructor<E> constructor;

        public Adapter(Context context, Class<E> holderClass)
        {
            this(context, 0, holderClass);
        }

        public Adapter(Context context, int layout, Class<E> holderClass)
        {
            this(context, layout, new ArrayList<T>(), holderClass);
        }

        public Adapter(Context context, int layout, List<T> items, Class<E> holderClass)
        {
            this.layout = layout;
            this.context = context;
            this.items = items;

            try
            {
                parameterTypes = new Class[]{Context.class, View.class};
                constructor = holderClass.getConstructor(parameterTypes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void add(T data)
        {
            add(data, items.size());
        }

        public void add(T data, int position)
        {
            items.add(data);
            notifyItemInserted(position);
        }

        public void remove(int position)
        {
            items.remove(position);
            notifyItemRemoved(position);
        }

        public void update(T data, int position)
        {
            items.set(position, data);
            notifyItemChanged(position);
        }

        public void updateAll(List<T> items)
        {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public final ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            ViewHolder<T> viewHolder = null;

            try
            {
                Object[] parameters = {context, view};
                viewHolder = constructor.newInstance(parameters);
                viewHolder.adapter = this;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder<T> holder, int position)
        {
            holder.onBind(items.get(position), position);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    holder.onClick(items.get(position), position);
                }
            });
            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    return holder.onLongClick(items.get(position), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public final int getItemViewType(int position) {
            return getItemViewLayout(items.get(position), position);
        }

        protected int getItemViewLayout(T data, int position)
        {
            return layout;
        }
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder
    {
        protected Context context;
        private Adapter adapter;

        public ViewHolder(Context context, View view)
        {
            super(view);
            this.context = context;
        }

        protected  <E extends View> E findViewById(@IdRes int id)
        {
            return itemView.findViewById(id);
        }

        protected void remove(int position)
        {
            adapter.remove(position);
        }

        protected void update(T data, int position)
        {
            adapter.update(data, position);
        }

        public abstract void onBind(T data, int position);

        public abstract void onClick(T data, int position);

        public abstract boolean onLongClick(T data, int position);
    }
}
