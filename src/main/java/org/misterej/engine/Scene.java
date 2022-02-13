package org.misterej.engine;

public abstract class Scene {

    public int id;
    public static int _id = 0;
    public Scene()
    {
        id = _id;
        _id++;
    }

    public abstract void update(float deltaTime);
    public abstract void init();
}
