package org.misterej.engine.renderer;

import org.joml.Vector2f;

public class Line2D {
    private Vector2f from;
    private Vector2f to;

    public Line2D(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }
}
