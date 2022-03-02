package org.misterej.game;

import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.Input;
import org.misterej.engine.components.Script;

public class TilePosScript extends Script {
    public TilePosScript(GameObject gameObject) {
        super(gameObject);
    }

    private final int snap_x = 64;
    private final int snap_y = 64;

    @Override
    public void update(float deltaTime) {
        float x = Input.MouseListener.getX();
        float y = Input.MouseListener.getY();
        gameObject.setPos(new Vector2f(x,-y));

        System.out.println(gameObject.getTransform().position.x);
    }

    @Override
    public void start() {

    }
}
