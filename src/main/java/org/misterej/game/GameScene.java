package org.misterej.game;

import org.joml.Vector2f;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.renderer.*;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.Logger;

import java.io.PipedOutputStream;

public class GameScene extends Scene {


    @Override
    public void update(float deltaTime) {
        this.renderer.prepare();

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
        }

        this.renderer.render();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        loadResources();

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png");
        for (int i = 0; i < 1280 / 16; i++)
        {
            for(int j = 0; j < 720 / 16; j++)
            {
                GameObject go = new GameObject("obj",new Transform(new Vector2f(i * 16,j * 16), new Vector2f(16,16), new Vector2f(1,1)));
                go.addComponent(new SpriteRenderer(spriteSheet.getSprite(16)));
                this.addGameObject(go);
            }
        }


        GameObject go = new GameObject("Player",new Transform(new Vector2f(500,500), new Vector2f(100,100), new Vector2f(1,1)));
        go.addComponent(new SpriteRenderer(spriteSheet.getSprite(96)));
        go.addComponent(new ScriptComponent(new PlayerController(go)));
        this.addGameObject(go);


    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/0x72_16x16DungeonTileset.v1.png"), 16, 16, 160, 0
    ));
    }

}
