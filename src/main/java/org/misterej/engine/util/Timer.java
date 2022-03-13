package org.misterej.engine.util;

public class Timer {
    private float interval;
    private float time;
    private boolean isTime = false;

    public Timer(float interval)
    {
        this.interval = interval;
    }

    public Timer()
    {
        this.interval = 1;
    }


    public void update(float deltaTime)
    {
        time += deltaTime;
        if(time > interval) isTime = true;
    }

    public boolean isTime()
    {
        return isTime;
    }

    public void reset()
    {
        time = 0;
        isTime = false;
    }

    public void setInterval(float interval)
    {
        this.interval = interval;
    }

    public float getInterval()
    {
        return interval;
    }
}
