package org.misterej.engine.scripts;

import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.Input;
import org.misterej.engine.components.Script;

public class TilePosScript extends Script {
    public TilePosScript(GameObject gameObject, float snap_x, float snap_y) {
        super(gameObject);
        this.snap_x = snap_x;
        this.snap_y = snap_y;
    }

    private final float snap_x;
    private final float snap_y;

    @Override
    public void update(float deltaTime) {
        float x = (int)Input.MouseListener.getXWorld();
        float y = (int)Input.MouseListener.getYWorld();
        x = x > 0 ? (x / snap_x) * snap_x : ((x / snap_x) * snap_x) - snap_x;
        y = y > 0 ? (y / snap_y) * snap_y : ((y / snap_y) * snap_y) - snap_y;
        gameObject.setPos(new Vector2f(x,y));
    }

    @Override
    public void start() {

    }
}
