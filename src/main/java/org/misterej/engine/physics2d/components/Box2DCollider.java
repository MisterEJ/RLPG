package org.misterej.engine.physics2d.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.misterej.engine.Component;
import org.misterej.engine.Config;
import org.misterej.engine.renderer.Color;
import org.misterej.engine.renderer.DebugDraw;

public class Box2DCollider extends Collider {

    private Vector2f halfSize = new Vector2f();
    private Vector2f origin = new Vector2f();
    public Vector3f color = new Vector3f(0,0,0);

    @Override
    public void update(float deltaTime) {
        if(Config.drawColliders)
            DebugDraw.addBox2D(new Vector2f(gameObject.getPosition()).add(getOffset()), halfSize, 0, color);
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
