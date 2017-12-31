package com.yarten.utils;

/**
 * Created by yfic on 2017/12/29.
 */

public abstract class LoopThread extends Thread
{
    public abstract void onRun();

    private long deltaT = 0;

    public LoopThread setRate(int Hz)
    {
        deltaT = 1000 / Hz;
        return this;
    }

    public LoopThread setPeriod(long ms)
    {
        deltaT = ms;
        return this;
    }

    @Override
    public void run()
    {
        while (true)
        {
            synchronized (this)
            {
                if(requireFinish) break;
            }

            long t1 = System.currentTimeMillis();
            onRun();
            long t2 = System.currentTimeMillis();

            long sleepTime = deltaT - t2 + t1;
            if(sleepTime > 0) try
            {
                Thread.sleep(sleepTime);
            }
            catch (Exception e){e.printStackTrace();}
        }

        synchronized (this)
        {
            isFinish = true;
        }
    }

    private boolean isFinish;
    private boolean requireFinish;

    @Override
    public synchronized void start()
    {
        requireFinish = false;
        isFinish = false;
        super.start();
    }

    public void quit()
    {
        synchronized (this)
        {
            requireFinish = true;
        }

        if(this == Thread.currentThread()) return;

        while (true)
        {
            synchronized (this)
            {
                if(isFinish) break;
            }
            try{Thread.sleep(1);}
            catch (Exception e){}
        }
    }
}
