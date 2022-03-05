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
        int x = (int)Input.MouseListener.getXWorld();
        int y = (int)Input.MouseListener.getYWorld();
        System.out.println(x);
        x = x > 0 ? (x / snap_x) * snap_x : ((x / snap_x) * snap_x) - snap_x;
        y = y > 0 ? (y / snap_y) * snap_y : ((y / snap_y) * snap_y) - snap_y;
        gameObject.setPos(new Vector2f(x,y));
    }

    @Override
    public void start() {

    }
}
