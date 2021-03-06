package org.misterej.engine.components;

import org.joml.Vector2f;
import org.misterej.engine.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private Texture texture;
    private List<Sprite> sprites;

    private Vector2f spritedimensions;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing)
    {
        this.spritedimensions = new Vector2f(spriteWidth, spriteHeight);
        this.sprites = new ArrayList<>();
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for(int i = 0; i < numSprites; i++)
        {
            // NORMALIZED DEVICE COORDINATES + half_pixel correction
            float topY = (currentY + spriteHeight - 0.5f) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth - 0.5f) / (float)texture.getWidth();
            float leftX = (currentX + 0.5f) / (float)texture.getWidth();
            float bottomY = (currentY + 0.5f) / (float)texture.getHeight();


            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.texture, texCoords);
            sprite.setHeight(spriteHeight);
            sprite.setWidth(spriteWidth);
            sprite.setId(i);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index)
    {
        return this.sprites.get(index);
    }

    public List<Sprite> getSprites()
    {
        return sprites;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f getSpritedimensions()
    {
        return spritedimensions;
    }

}
