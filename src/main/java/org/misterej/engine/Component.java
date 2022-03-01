package org.misterej.engine;

public abstract class Component {

    public GameObject gameObject;

    public void imgui()
    {

    }
    public abstract void update(float deltaTime);
    public abstract void start();
}
