package org.misterej.engine.components;

import org.joml.Vector4f;
import org.misterej.engine.Component;
import org.misterej.engine.renderer.Color;

public class SpriteRenderer extends Component {

    private Color color;

    public SpriteRenderer(Color color)
    {
        this.color = color;
    }

    @Override
    public void start(){

    }

    @Override
    public void update(float deltaTime) {

    }

    public Color getColor() {
        return color;
    }
}
