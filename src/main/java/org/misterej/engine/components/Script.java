package org.misterej.engine.components;

import org.misterej.engine.GameObject;

public abstract class Script {
    protected GameObject gameObject;

    public Script(GameObject gameObject)
    {
        this.gameObject = gameObject;
    }

    public abstract void update(float deltaTime);
    public abstract void start();
}
