package com.yarten.editor;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.airbubble.AirBubble;
import com.yarten.sgbutton.SGWidget;
import com.yarten.sgbutton.SGWidgetButton;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Utils;
import com.yarten.editor.WidgetManager.Widget;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2018/1/1.
 */

public class DrawerAdapter extends CommonRecyclerView.Adapter<Widget, DrawerAdapter.ViewHolder>
{
    private ViewGroup tempLayout;
    private Context context;
    private Listener listener;

    public DrawerAdapter(Context context, ViewGroup tempLayout, Listener listener)
    {
        this(context, tempLayout, new ArrayList<Widget>(), listener);
    }

    public DrawerAdapter(Context context, ViewGroup tempLayout, List<Widget> items, Listener listener)
    {
        super(context, R.layout.item_layout, items, ViewHolder.class);
        this.tempLayout = tempLayout;
        this.context = context;
        this.listener = listener;
    }

    public interface Listener
    {
        void onCreateView(Widget widget);

        void onDragBegin();
    }

    @Override
    public void onBindViewHolder(final CommonRecyclerView.ViewHolder<Widget> holder, int position)
    {
        super.onBindViewHolder(holder, position);
        final ViewHolder viewHolder = (ViewHolder)holder;

        SGWidgetButton sgWidgetButton = new SGWidgetButton(viewHolder.view, tempLayout);
        sgWidgetButton.setOnClickListener(new SGWidget.OnActionListener() {
            @Override
            public void onAction(View view, MotionEvent event)
            {
                AirBubble airBubble = new AirBubble(context);
                airBubble.setText(viewHolder.widget.description);
                tempLayout.removeAllViews();
                tempLayout.addView(airBubble);
                airBubble.setX(viewHolder.itemView.getX() + Utils.dip2px(context, 120));
                airBubble.setY(viewHolder.itemView.getY() + Utils.dip2px(context, 15) );
            }
        });
        sgWidgetButton.setOnAddHandler(new SGWidgetButton.OnAddHandler() {
            @Override
            public View createNewView(int userdata) {
                listener.onDragBegin();
                View view = viewHolder.clone();
                tempLayout.removeAllViews();
                return view;
            }
        });
        sgWidgetButton.setOnFinishHandler(new SGWidgetButton.OnFinishHandler() {
            @Override
            public void postFinish(View newView) {
                if(listener != null)
                {
                    Widget widget = viewHolder.widget.clone();
                    widget.style.x = newView.getX();
                    widget.style.y = newView.getY();
                    listener.onCreateView(widget);
                }
                tempLayout.removeAllViews();

            }
        });
    }

    @Override
    protected int getItemViewLayout(Widget data, int position) {
        switch (data.type)
        {
            case Button:
                return R.layout.button_item;
            default:
                return super.getItemViewLayout(data, position);
        }
    }



    public static class ViewHolder extends CommonRecyclerView.ViewHolder<Widget>
    {
        private Widget widget = null;
        private View view = null;

        public ViewHolder(Context context, View view) {
            super(context, view);
            this.view = view.findViewById(R.id.item);
        }

        @Override
        public void onClick(Widget data, int position) {}

        @Override
        public boolean onLongClick(Widget data, int position) {
            return false;
        }

        @Override
        public void onBind(Widget data, int position)
        {
            widget = data;
            WidgetManager.tryStylize(view, widget.style);
        }

        public View clone()
        {
            return WidgetManager.createView(context, widget);
        }
    }
}
