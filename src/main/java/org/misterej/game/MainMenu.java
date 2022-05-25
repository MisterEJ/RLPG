package org.misterej.game;

import org.joml.Vector2f;
import org.misterej.engine.*;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.util.AssetPool;

public class MainMenu extends Scene {

    public MainMenu(String level) {
        super(level);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void init() {
        AssetPool.addSpriteSheet("assets/textures/text.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/text.png"),
                48, 16, 5, 0
        ));

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/text.png");

        for(int i = 0; i < 5; i++)
        {
            float textHeight = spriteSheet.getSpritedimensions().y / spriteSheet.getSpritedimensions().y;
            float textWidth = spriteSheet.getSpritedimensions().x / spriteSheet.getSpritedimensions().y;

            float x = Config.view_width / 2;
            float y = Config.view_height / 2;

            y -= i * textHeight;

            Transform transform = new Transform(new Vector2f(x,y), new Vector2f(textWidth, textHeight));
            GameObject gameObject = new GameObject("MenuItem: " + i, transform);
            gameObject.addComponent(new SpriteRenderer(spriteSheet.getSprite(i)));
            this.addGameObject(gameObject);
        }

    }
}
