package org.misterej.engine.physics2d.components;

import org.joml.Vector2f;
import org.misterej.engine.Component;

public abstract class Collider extends Component {
    private Vector2f offset = new Vector2f();

    public Vector2f getOffset()
    {
        return offset;
    }
}
