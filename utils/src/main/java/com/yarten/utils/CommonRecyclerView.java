package com.yarten.utils;

import android.content.Context;
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

    public  CommonRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setItemAnimator(new DefaultItemAnimator());
        setLayoutManager(new LinearLayoutManager(context));
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder>
    {
        public interface Listener
        {
            void onBind(View view, int position);

            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }

        private int size = 0;
        private Listener listener;
        private int layout;
        private Context context;
        private Constructor constructor;
        private Object[] parameters;

        public Adapter(Context context, int layout, Listener listener)
        {
            this(context, layout, 0, listener);
        }

        public Adapter(Context context, int layout, int size, Listener listener)
        {
            this.layout = layout;
            this.listener = listener;
            this.context = context;
            this.size = size;

            try
            {
                Class listenerClass = Class.forName(listener.getClass().getName());
                constructor = listenerClass.getConstructors()[0];
                parameters = new Object[]{context};
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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(layout, null);
            ViewHolder viewHolder = null;

            try
            {
                Listener listener = (Listener) constructor.newInstance(parameters);
                viewHolder = new ViewHolder(view, listener);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if(holder.listener != null)
            {
                holder.listener.onBind(holder.itemView, position);
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.listener.onClick(v, holder.getAdapterPosition());
                    }
                });
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        holder.listener.onLongClick(v, holder.getAdapterPosition());
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        Adapter.Listener listener;

        ViewHolder(View view, Adapter.Listener listener)
        {
            super(view);
            this.listener = listener;
        }
    }
}
