package org.misterej.engine.dyn4j.components;

public class CircleCollider extends Collider{

    private double radius;

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void start() {

    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
