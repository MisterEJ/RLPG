package org.misterej.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.misterej.engine.Component;
import org.misterej.engine.SceneManager;
import org.misterej.engine.renderer.Color;
import org.misterej.engine.renderer.Texture;
import org.misterej.engine.util.Logger;

public class SpriteRenderer extends Component {

    private Color color;
    private Sprite sprite;
    private int zIndex = 0;

    private boolean dirty = true;

    public SpriteRenderer(Color color)
    {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
        this.color = Color.White;
    }

    @Override
    public void start(){

    }

    @Override
    public void update(float deltaTime) {

    }

    public Texture getTexture()
    {
        return this.sprite.getTexture();
    }

    public Vector2f[] getTexCoords()
    {
        return this.sprite.getTexCoords();
    }

    public Color getColor() {
        return color;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void resetDirtyFlag()
    {
        dirty = false;
    }

    public void setDirtyFlag()
    {
        dirty = true;
    }

    public void setColor(Color color)
    {
        this.color = color;
        dirty = true;
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
        dirty = true;
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public int getZIndex()
    {
        return zIndex;
    }

    public void setZIndex(int index)
    {
        this.zIndex = index;
    }
}
