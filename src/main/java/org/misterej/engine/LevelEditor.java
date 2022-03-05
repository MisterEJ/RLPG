package org.misterej.engine;

import org.joml.Vector2f;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.imgui.ImGuiLayer;
import org.misterej.engine.renderer.DebugDraw;
import org.misterej.engine.util.AssetPool;

import java.util.List;

public class LevelEditor extends Scene{

    private Renderer renderer = new Renderer();
    private List<GameObject> gameObjects;
    private ImGuiLayer imguilayer;
    private Tilemap tilemap;

    @Override
    public void update(float deltaTime) {
        imguilayer.update(deltaTime);
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f());
        imguilayer = new ImGuiLayer(Window.getWindow());
        imguilayer.init();

        AssetPool.addSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/0x72_16x16DungeonTileset.v1.png"), 16, 16, 160, 0
        ));

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/0x72_16x16DungeonTileset.v1.png");
        tilemap = new Tilemap(64,64, spriteSheet);
        tilemap.load("assets/level1.csv");
        this.addTilemap(tilemap);

        for(int i = -25; i < 25; i++)
        {
            DebugDraw.addLine2D(new Vector2f(-100000,i * tilemap.getCellHeight()), new Vector2f(100000, i * tilemap.getCellHeight()));
            DebugDraw.addLine2D(new Vector2f(i * tilemap.getCellWidth(),100000), new Vector2f(i * tilemap.getCellWidth(), -100000));
        }
    }
}
