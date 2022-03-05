package org.misterej.game;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.misterej.engine.*;
import org.misterej.engine.components.Script;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.renderer.Color;

public class PlayerController extends Script {
    public PlayerController(GameObject gameObject) {
        super(gameObject);
    }

    private Camera camera;

    private SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
    private float speed = 100f;

    @Override
    public void update(float deltaTime) {
        if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_A))
            gameObject.move(new Vector2f(speed * -deltaTime, 0));
        if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_D))
            gameObject.move(new Vector2f(speed * deltaTime, 0));
        if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_W))
            gameObject.move(new Vector2f(0, speed * deltaTime));
        if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_S))
            gameObject.move(new Vector2f(0, speed * -deltaTime));

       camera.position.x = gameObject.getTransform().position.x - (camera.getViewPort().x / 2f) + (gameObject.getTransform().size.x / 2f);
       camera.position.y = gameObject.getTransform().position.y - (camera.getViewPort().y / 2f) + (gameObject.getTransform().size.y / 2f);
    }

    @Override
    public void start() {
        camera = SceneManager.getCurrentScene().getCamera();
    }
}
