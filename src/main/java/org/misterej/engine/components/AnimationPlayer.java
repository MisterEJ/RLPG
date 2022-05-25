package org.misterej.engine.components;

import org.misterej.engine.Component;

import java.util.HashMap;
import java.util.Map;

public class AnimationPlayer extends Component {

    private Map<String, Animation> animations = new HashMap<>();
    private SpriteRenderer spriteRenderer;
    private Animation animation;

    @Override
    public void update(float deltaTime) {
        if(animation != null && animation.isPlaying())
        {
            animation.update(deltaTime);
            spriteRenderer.setSprite(animation.getFrame());
        } else if(animation != null)
        {
            spriteRenderer.setSprite(animation.getFrame());
        }
    }

    @Override
    public void start() {
        spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        assert spriteRenderer != null : "(AnimatedSprite): " + gameObject.getName() + ": Can't find sprite renderer";
    }

    public void play()
    {
        if(animation == null) assert false : "(AnimationPlayer): Animation not set";
        animation.play();
    }

    public void stop()
    {
        if(animation == null) assert false : "(AnimationPlayer): Animation not set";
        animation.stop();
    }

    public void setAnimation(String animationName, boolean play)
    {
        if(animations.containsKey(animationName))
        {
            if(!(animation == animations.get(animationName)))
            {
                if(!(animation == null)) animation.stop();
                animation = animations.get(animationName);
                if (play) animation.play();
            }
        }
    }

    public void setFrame(int frame)
    {
        animation.setFrame(frame);
    }

    public String getAnimationName()
    {
        return animation.getName();
    }

    public void addAnimation(Animation animation)
    {
        if(animations.containsKey(animation.getName())) return;
        animations.put(animation.getName(), animation);
    }

    public boolean isPlaying()
    {
        return animation.isPlaying();
    }

}
