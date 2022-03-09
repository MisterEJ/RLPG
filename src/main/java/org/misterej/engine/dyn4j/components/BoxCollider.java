package org.misterej.engine.dyn4j.components;

import org.dyn4j.geometry.Vector2;

public class BoxCollider extends Collider{

    private Vector2 size;

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void start() {

    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
