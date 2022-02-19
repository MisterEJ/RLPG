package org.misterej.game;

import org.misterej.engine.GameObject;
import org.misterej.engine.components.Script;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.renderer.Color;

public class PlayerController extends Script {
    public PlayerController(GameObject gameObject) {
        super(gameObject);
    }

    private SpriteRenderer spr;

    @Override
    public void update(float deltaTime) {
        gameObject.transfrom.position.x += 10 * deltaTime;
    }

    @Override
    public void start() {
        spr = gameObject.getComponent(SpriteRenderer.class);
        spr.setColor(new Color(0.5f, 0.2f, 0.7f, 1.0f));
    }
}
