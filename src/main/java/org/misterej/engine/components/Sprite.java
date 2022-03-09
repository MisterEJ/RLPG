package org.misterej.engine.components;

import org.joml.Vector2f;
import org.misterej.engine.renderer.Texture;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCoords;
    private int id = 0;

    private float width, height;

    public Sprite(Texture texture)
    {
        this.texture = texture;
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        this.texCoords = texCoords;

    }

    public Sprite(Texture texture, Vector2f[] texCoords)
    {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public int getTexID()
    {
        return texture.getTexID();
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
         this.id = id;
    }
}
