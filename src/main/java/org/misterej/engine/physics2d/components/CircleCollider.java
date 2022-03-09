package org.misterej.engine.physics2d.components;

import org.misterej.engine.Component;

public class CircleCollider extends Collider {

    private float radius = 1f;

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void start() {

    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
