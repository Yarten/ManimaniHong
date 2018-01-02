package com.yarten.editor;

import android.content.Context;
import android.support.constraint.ConstraintSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.airbubble.AirBubble;
import com.yarten.sgbutton.SGWidget;
import com.yarten.sgbutton.SGWidgetButton;
import com.yarten.shapebutton.ShapeButton;
import com.yarten.utils.CommonRecyclerView;
import com.yarten.utils.Interface.Styleable;
import com.yarten.utils.Style;
import com.yarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2018/1/1.
 */

public class DrawerAdapter extends CommonRecyclerView.Adapter<DrawerAdapter.Widget, DrawerAdapter.ViewHolder>
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
        void onCreateView(View view, ViewGroup.LayoutParams params, float x, float y);

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
                    listener.onCreateView(viewHolder.clone(), newView.getLayoutParams(), newView.getX(), newView.getY());
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

    public static class Widget
    {
        public enum Type
        {
            Button
        }

        public static final int WIDGET_LENGTH = 100;
        public static final int BUTTON_LENGTH = 100;
        public static Style BUTTON_STYLE;

        public Type type;
        public String name;
        public String description;
        public Style style;

        static
        {
            BUTTON_STYLE = new Style();
            BUTTON_STYLE.color = Styleable.Color.Blue;
            BUTTON_STYLE.shape = Styleable.Shape.Circle;
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
            if(view instanceof Styleable)
            {
                widget.style.stylize((Styleable)view);
            }
        }


        public View clone()
        {
            View view = null;
            int length = Widget.WIDGET_LENGTH;
            switch (widget.type)
            {
                case Button:
                    view = new ShapeButton(context);
                    length = Widget.BUTTON_LENGTH;
                    break;
            }

            length = Utils.dip2px(context, length);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(length, length);
            view.setLayoutParams(params);

            if(view instanceof Styleable)
            {
                widget.style.stylize((Styleable)view);
            }

            return view;
        }
    }
}
