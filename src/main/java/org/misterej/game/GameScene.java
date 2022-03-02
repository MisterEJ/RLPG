package org.misterej.game;

import org.joml.Vector2f;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.renderer.*;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.Logger;

import java.io.PipedOutputStream;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {

    Tilemap tilemap;

    @Override
    public void update(float deltaTime) {

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
        }

        this.renderer.render();

        if(Input.KeyboardListener.iskeyPressed(GLFW_KEY_H))
        {
            DebugDraw.debugPrint();
            System.out.println("H");
        }
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        loadResources();

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png");
        tilemap = new Tilemap(64,64, spriteSheet);
        tilemap.load("assets/level1.csv");
        this.addTilemap(tilemap);

        GameObject go = new GameObject("Player",new Transform(new Vector2f(0,0), new Vector2f(100,100), new Vector2f(1,1)));
        go.addComponent(new SpriteRenderer(spriteSheet.getSprite(96)));
        go.addComponent(new ScriptComponent(new PlayerController(go)));
        this.addGameObject(go);

        GameObject go1 = new GameObject("Player",new Transform(new Vector2f(200,200), new Vector2f(64,64), new Vector2f(1,1)));
        go1.addComponent(new SpriteRenderer(spriteSheet.getSprite(16)));
        go1.addComponent(new ScriptComponent(new TilePosScript(go1)));
        this.addGameObject(go1);

        //DebugLines
        for(int i = -25; i < 25; i++)
        {
            DebugDraw.addLine2D(new Vector2f(-100000,i * tilemap.getCellHeight()), new Vector2f(100000, i * tilemap.getCellHeight()));
            DebugDraw.addLine2D(new Vector2f(i * tilemap.getCellWidth(),100000), new Vector2f(i * tilemap.getCellWidth(), -100000));
        }
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/0x72_16x16DungeonTileset.v1.png"), 16, 16, 160, 0
    ));
    }

}
