package org.misterej.game;

import org.joml.Vector2f;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.renderer.*;

public class GameScene extends Scene {


    @Override
    public void update(float deltaTime) {

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
            renderer.updateSprite(go.getComponent(SpriteRenderer.class));
        }

        this.renderer.render();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int width = 16;
        int height = 16;

        for (int i = 0; i < 1280 / width; i++)
        {
            for(int j = 0; j < 720 / height; j++)
            {
                GameObject go = new GameObject("OBJ" + i + " " + j, new Transform(new Vector2f(i * width, j * width), new Vector2f(width,height)));
                go.addComponent(new SpriteRenderer(Color.Blue));
                go.addComponent(new ScriptComponent(new PlayerController(go)));
                this.addGameObject(go);
            }
        }
    }

}
