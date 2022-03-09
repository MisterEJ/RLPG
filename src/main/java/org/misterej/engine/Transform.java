package org.misterej.engine;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;
    public Vector2f size;
    public float rotation = 0.0f;

    public Transform()
    {
        init(new Vector2f(), new Vector2f(), new Vector2f(1,1));
    }

    public Transform(Vector2f position)
    {
        init(position, new Vector2f(), new Vector2f(1,1));
    }

    public Transform(Vector2f position, Vector2f size)
    {
        init(position, size, new Vector2f(1,1));
    }

    public Transform(Vector2f position, Vector2f size, Vector2f scale)
    {
        init(position, size, scale);
    }

    public void init(Vector2f position, Vector2f size, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
        this.size = size;
    }
}
