package com.yarten.sgbutton;


/**
 * Created by yarten on 2017/10/6.
 * 常用的数学函数 —— 继承自MathFunction
 * 描述：
 *     事实上，就是为MathFunction设置了一个函数，
 * 并且维护跟这个函数有关的参数。其中，除了Linear线
 * 性函数的参数是斜率和截距，Elastic多了一个周期外，
 * 其他所有函数均有两个控制参数：e, c
 * 1. e: expansion rate, 指区间的缩放率，取值0~2，
 *      默认是1；
 * 2. c: center rate, 指离默认中心的远近，取值-1~1,
 *      默认是0，该中心是函数的对称中心。
 * Note: 本类族参考cocos
 */


class LinearFunction extends MathFunction
{
    public LinearFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public LinearFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 1, 0);
    }

    public LinearFunction(long duration, float rangeMin, float rangeMax, float k, float b)
    {
        super(duration, 0, 100, 0, 0, rangeMin, rangeMax);
        reset(k, b);
    }

    private float k, b;

    public void reset(float k, float b)
    {
        this.k = k;
        this.b = b;
        Timing func = new Timing()
        {
            @Override
            public float f(float t)
            {
                return LinearFunction.this.k*t+LinearFunction.this.b;
            }
        };

        super.setTimingFunction(func);
        super.setCodomain(func.f(0), func.f(100));
    }
}

class ExponentialFunction extends MathFunction
{
    public ExponentialFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public ExponentialFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 0, 1);
    }

    public ExponentialFunction(long duration, float rangeMin, float rangeMax, float c, float e)
    {
        super(duration, 0, 4, 0, 10, rangeMin, rangeMax);
        reset(c, e);
    }


    public void reset(float c, float e)
    {
        Timing func = new Timing()
        {
            @Override
            public float f(float t)
            {
                if(t <= 2.0)
                    return (float)(Math.pow(2, 10*(t-1)) * 0.005d);
                else
                {
                    t -= 2.0f;
                    return (float)(-Math.pow(2, 10*(1-t)) * 0.005d + 10.0d);
                }
            }
        };

        if(e > 2 || e < 0) e = 1;
        if(c > 1 || c < -1) c = 0;
        e *= 2.0f;
        c = 2.0f * (1.0f+c);
        super.setDomian(c-e, c+e);
        super.setCodomain(func.f(c-e), func.f(c+e));
        super.setTimingFunction(func);
    }
}

class BounceFunction extends MathFunction
{
    public BounceFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public BounceFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 0, 1);
    }

    public BounceFunction(long duration, float rangeMin, float rangeMax, float c, float e)
    {
        super(duration, 0, 0, 0, 0, rangeMin, rangeMax);
        reset(c, e);
    }

    public void reset(float c, float e)
    {
        Timing func = new Timing()
        {
            @Override
            public float f(float time)
            {
                float newT = 0;
                if (time < 0.5f)
                {
                    time = time * 2;
                    newT = (1 - bounceTime(1 - time)) * 0.5f;
                }
                else
                {
                    newT = bounceTime(time * 2 - 1) * 0.5f + 0.5f;
                }

                return newT;
            }
        };

        if(e > 2 || e < 0) e = 1;
        if(c > 1 || c < -1) c = 0;
        e *= 0.5f;
        c = 0.5f * (1.0f+c);
        super.setDomian(c-e, c+e);
        super.setCodomain(func.f(c-e), func.f(c+e));
        super.setTimingFunction(func);
    }

    private float bounceTime(float time)
    {
        if (time < 1 / 2.75)
        {
            return 7.5625f * time * time;
        }
        else if (time < 2 / 2.75)
        {
            time -= 1.5f / 2.75f;
            return 7.5625f * time * time + 0.75f;
        }
        else if(time < 2.5 / 2.75)
        {
            time -= 2.25f / 2.75f;
            return 7.5625f * time * time + 0.9375f;
        }

        time -= 2.625f / 2.75f;
        return 7.5625f * time * time + 0.984375f;
    }
}

class BackEaseFunction extends MathFunction
{
    public BackEaseFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public BackEaseFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 0, 1);
    }

    public BackEaseFunction(long duration, float rangeMin, float rangeMax, float c, float e)
    {
        super(duration, 0, 0, 0, 0, rangeMin, rangeMax);
        reset(c, e);
    }

    public void reset(float c, float e)
    {
        Timing func = new Timing()
        {
            @Override
            public float f(float time)
            {
                float overshoot = 1.70158f * 1.525f;

                if (time < 1)
                {
                    return (time * time * ((overshoot + 1) * time - overshoot)) / 2;
                }
                else
                {
                    time = time - 2;
                    return (time * time * ((overshoot + 1) * time + overshoot)) / 2 + 1;
                }
            }
        };

        if(e > 2 || e < 0) e = 1;
        if(c > 1 || c < -1) c = 0;
        e *= 1.0f;
        c = 1.0f * (1.0f+c);
        super.setDomian(c-e, c+e);
        super.setCodomain(func.f(c-e), func.f(c+e));
        super.setTimingFunction(func);
    }
}

class ElasticFunction extends MathFunction
{
    public ElasticFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public ElasticFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 0.3f*1.5f, 0, 1);
    }

    public ElasticFunction(long duration, float rangeMin, float rangeMax, float period, float c, float e)
    {
        super(duration, 0, 0, 0, 0, rangeMin, rangeMax);
        reset(period, c, e);
    }

    public void reset(float period)
    {
        reset(period, c, e);
    }

    private float c, e, period;

    public void reset(float period, float c, float e)
    {
        Timing func = new Timing()
        {
            @Override
            public float f(float time)
            {
                float newT, period = ElasticFunction.this.period;
                if (time == 0 || time == 1)
                {
                    newT = time;
                }
                else
                {
                    time = time * 2;
                    if (period == 0)
                    {
                        period = 0.3f * 1.5f;
                    }

                    float s = period / 4;

                    time = time - 1;
                    if (time < 0)
                    {
                        newT = (float)(-0.5f * Math.pow(2, 10 * time) * Math.sin((time -s) * Math.PI * 2.0f / period));
                    }
                    else
                    {
                        newT = (float)(Math.pow(2, -10 * time) * Math.sin((time - s) * Math.PI * 2.0f / period) * 0.5f + 1);
                    }
                }
                return newT;
            }
        };

        this.c = c;
        this.e = e;
        this.period = period;
        if(e > 2 || e < 0) e = 1;
        if(c > 1 || c < -1) c = 0;
        e *= 0.5f;
        c = 0.5f * (1.0f+c);
        super.setDomian(c-e, c+e);
        super.setCodomain(func.f(c-e), func.f(c+e));
        super.setTimingFunction(func);
    }
}

class SinFunction extends MathFunction
{
    public SinFunction(long duration)
    {
        this(duration, 0, 100);
    }

    public SinFunction(long duration, float rangeMin, float rangeMax)
    {
        this(duration, rangeMin, rangeMax, 0, 1);
    }

    public SinFunction(long duration, float rangeMin, float rangeMax, float c, float e)
    {
        super(duration, 0, 0, 0, 0, rangeMin, rangeMax);
        reset(c, e);
    }

    public void reset(float c, float e)
    {
        Timing func = new Timing()
        {
            @Override
            public float f(float time)
            {
                return (float)(-0.5f * (Math.cos((float)Math.PI * time) - 1));
            }
        };

        if(e > 2 || e < 0) e = 1;
        if(c > 1 || c < -1) c = 0;
        e *= 0.5f;
        c = 0.5f * (1.0f+c);
        super.setDomian(c-e, c+e);
        super.setCodomain(func.f(c-e), func.f(c+e));
        super.setTimingFunction(func);
    }
}