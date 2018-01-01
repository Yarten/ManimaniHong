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
import java.lang.reflect.Constructor;

/**
 * Created by yfic on 2017/12/31.
 */

public class CommonRecyclerView extends RecyclerView
{
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
        setLayoutManager(new LinearLayoutManager(context));
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder>
    {
        private int size = 0;
        private int layout;
        private Context context;
        private Class[] parameterTypes;
        private Constructor constructor;

        public Adapter(Context context, int layout, Class holderClass)
        {
            this(context, layout, 0, holderClass);
        }

        public Adapter(Context context, int layout, int size, Class holderClass)
        {
            this.layout = layout;
            this.context = context;
            this.size = size;

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

        public void add(int position)
        {
            size += 1;
            notifyItemInserted(position);
        }

        public void remove(int position)
        {
            size -= 1;
            notifyItemRemoved(position);
        }

        public void update(int position)
        {
            notifyItemChanged(position);
        }

        public void updateAll(int size)
        {
            this.size = size;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(layout, null);
            ViewHolder viewHolder = null;

            try
            {
                Object[] parameters = {context, view};
                viewHolder = (ViewHolder) constructor.newInstance(parameters);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.onBind(position);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder
    {
        protected Context context;

        public ViewHolder(Context context, View view)
        {
            super(view);
            this.context = context;
        }

        protected  <T extends View> T findViewById(@IdRes int id)
        {
            return itemView.findViewById(id);
        }

        public abstract void onBind(int position);

        public abstract void onClick(int position);

        public abstract boolean onLongClick(int position);
    }
}
