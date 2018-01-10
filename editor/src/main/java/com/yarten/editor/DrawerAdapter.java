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
import com.yarten.widget.Widget;
import com.yarten.widget.WidgetManager;

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
    private AirBubble airBubble;

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

        airBubble = new AirBubble(context);
        airBubble.setVisibility(View.GONE);
        airBubble.setY(-1);
    }

    public interface Listener
    {
        void onCreateView(Widget widget);

        void onDragBegin();

        void onItemDown();

        void onItemUp();
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
                float y = viewHolder.itemView.getY() + Utils.dip2px(context, 15);
                if(airBubble.getVisibility() == View.GONE || airBubble.getY() != y)
                {
                    if(airBubble.getParent() == null) tempLayout.addView(airBubble);
                    airBubble.setText(viewHolder.widget.description);
                    airBubble.setText(viewHolder.widget.description);
                    airBubble.setX(viewHolder.itemView.getX() + Utils.dip2px(context, 120));
                    airBubble.setY(y);
                    airBubble.setVisibility(View.VISIBLE);
                }
                else
                {
                    cleanUp();
                }
            }
        });
        sgWidgetButton.setOnAddHandler(new SGWidgetButton.OnAddHandler() {
            @Override
            public View createNewView(int userdata) {
                listener.onDragBegin();
                View view = viewHolder.clone();
                cleanUp();
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
                cleanUp();
            }
        });
        sgWidgetButton.setOnDownListener(new SGWidget.OnActionListener() {
            @Override
            public void onAction(View view, MotionEvent event) {
                listener.onItemDown();
            }
        });
        sgWidgetButton.setOnUpListener(new SGWidget.OnActionListener() {
            @Override
            public void onAction(View view, MotionEvent event) {
                listener.onItemUp();
            }
        });
    }

    public void cleanUp()
    {
        tempLayout.removeAllViews();
        hideBubble();
    }

    public void hideBubble()
    {
        airBubble.setVisibility(View.GONE);
        airBubble.setY(-1);
    }

    @Override
    protected int getItemViewLayout(Widget data, int position) {
        switch (data.type)
        {
            case Button:
                return R.layout.item_button;
            case Rocker:
                return R.layout.item_rocker;
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
