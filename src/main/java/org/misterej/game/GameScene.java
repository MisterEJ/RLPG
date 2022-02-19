package org.misterej.game;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.misterej.engine.Camera;
import org.misterej.engine.GameObject;
import org.misterej.engine.Scene;
import org.misterej.engine.Transform;
import org.misterej.engine.components.ScriptComponent;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.renderer.Color;

import java.io.PipedOutputStream;

public class GameScene extends Scene {


    @Override
    public void update(float deltaTime) {

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
            renderer.updateSprite(go);
        }

        this.renderer.render();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject go = new GameObject("OBJ", new Transform(new Vector2f(0.0f, 0.0f), new Vector2f(100, 100)));
        go.addComponent(new SpriteRenderer(Color.Black));
        go.addComponent(new ScriptComponent(new PlayerController(go)));
        this.addGameObject(go);

    }

}
