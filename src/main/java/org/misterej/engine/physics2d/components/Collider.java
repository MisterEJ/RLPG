package org.misterej.engine.physics2d.components;

import org.joml.Vector2f;
import org.misterej.engine.Component;

public abstract class Collider extends Component {
    private Vector2f offset = new Vector2f();
    private boolean isSensor = false;

    public Vector2f getOffset()
    {
        return offset;
    }

    public void setOffset(Vector2f offset)
    {
        this.offset = offset;
    }

    public boolean isSensor()
    {
        return isSensor;
    }

    public void setSensor(boolean isSensor)
    {
        this.isSensor = isSensor;
    }
}
