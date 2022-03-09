package org.misterej.engine.physics2d.components;

import org.joml.Vector2f;
import org.misterej.engine.Component;

public class Box2DCollider extends Collider {

    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void start() {

    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2f origin)
    {
        this.origin.x = origin.x;
        this.origin.y = origin.y;
    }
}
