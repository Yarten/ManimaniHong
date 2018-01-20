package com.yarten.mainmenu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2017/12/26.
 */

public class ViewPagerHelper
{
    private Context context;
    private ArrayList<InitHandler> handlers;
    private ArrayList<Integer> layouts;
    private ArrayList<Integer> triggers;
    private ArrayList<OnTriggerListener> onTriggerListeners;

    public ViewPagerHelper(Context context)
    {
        this.context = context;
        handlers = new ArrayList<>();
        layouts = new ArrayList<>();
        triggers = new ArrayList<>();
        onTriggerListeners = new ArrayList<>();
    }

    public ViewPagerHelper initLayout(int layout, int trigger, InitHandler handler, OnTriggerListener onTriggerListener)
    {
        layouts.add(layout);
        triggers.add(trigger);
        handlers.add(handler);
        onTriggerListeners.add(onTriggerListener);
        return this;
    }

    public void build(int view_pager)
    {
        List<View> views = new ArrayList<>();
        AppCompatActivity activity = (AppCompatActivity)context;
        NoScrollViewPager viewPager = activity.findViewById(view_pager);

        for(int i = 0, size = layouts.size(); i< size; i++)
        {
            View view = LayoutInflater.from(context).inflate(layouts.get(i), null);
            View trigger = activity.findViewById(triggers.get(i));

            views.add(view);
            handlers.get(i).onInit(view);

            trigger.setOnClickListener(new OnChangeListener(viewPager, i, onTriggerListeners.get(i)));
        }

        viewPager.setAdapter(new Adapter(views));
        viewPager.setNoScroll(true);
    }

    public interface InitHandler
    {
        void onInit(View view);
    }

    public interface OnTriggerListener
    {
        boolean onTrigger();
    }

    class OnChangeListener implements View.OnClickListener
    {
        private ViewPager viewPager;
        private int position;
        private OnTriggerListener onTriggerListener;

        OnChangeListener(ViewPager viewPage, int position, OnTriggerListener onTriggerListener)
        {
            this.viewPager = viewPage;
            this.position = position;
            this.onTriggerListener = onTriggerListener;
        }

        @Override
        public void onClick(View v)
        {
            if(onTriggerListener == null || onTriggerListener.onTrigger())
                viewPager.setCurrentItem(position);
        }
    }

    class Adapter extends PagerAdapter
    {
        private List<View> views;

        public Adapter(List<View> views)
        {
            this.views = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
