package org.misterej.engine.components;

import org.misterej.engine.util.Timer;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private String name;
    private List<Sprite> frames;
    private int frame = 0;
    private SpriteSheet spriteSheet;
    private boolean isPlaying = false;
    private Timer timer = new Timer(1f/12f);
    private boolean loop;

    public Animation(String name, SpriteSheet spriteSheet, boolean loop, int[] frames)
    {
        this.loop = loop;
        this.name = name;
        this.frames = new ArrayList<>();
        this.spriteSheet = spriteSheet;
        addFrames(frames);
    }

    public Animation(String name, SpriteSheet spriteSheet, boolean loop)
    {
        this.loop = loop;
        this.name = name;
        this.frames = new ArrayList<>();
        this.spriteSheet = spriteSheet;
    }

    public Animation(String name, SpriteSheet spriteSheet)
    {
        this.loop = false;
        this.name = name;
        this.frames = new ArrayList<>();
        this.spriteSheet = spriteSheet;
    }

    public void setSpeed(float speed)
    {
        timer.setInterval(1f / speed);
    }

    public float getSpeed()
    {
        return timer.getInterval();
    }

    public int getFrameCount()
    {
        return frames.size();
    }

    public Sprite getFrame()
    {
        return frames.get(frame);
    }

    public void update(float deltaTime)
    {
        if(isPlaying)
        {
            timer.update(deltaTime);
            if(timer.isTime())
            {
                timer.reset();
                if(frame < getFrameCount() - 1)
                {
                    frame++;
                } else if(loop)
                {
                    frame = 0;
                } else
                {
                    isPlaying = false;
                }
            }
        }
    }

    public void setFrame(int frame)
    {
        if(frame < frames.size() && frame >= 0)
            this.frame = frame;
        else this.frame = 0;
    }

    public void play()
    {
        frame = 0;
        isPlaying = true;
    }

    public void stop()
    {
        isPlaying = false;
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public String getName()
    {
        return name;
    }

    public void addFrame(int frame)
    {
        frames.add(spriteSheet.getSprite(frame));
    }

    public void addFrames(int[] frames)
    {
        for(int frame : frames)
        {
            addFrame(frame);
        }
    }

}
