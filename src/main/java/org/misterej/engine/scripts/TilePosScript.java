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

        float x = Input.MouseListener.getXWorld() > 0 ? (int)Input.MouseListener.getXWorld() : (int)Input.MouseListener.getXWorld() - snap_x;
        float y = Input.MouseListener.getYWorld() > 0 ? (int)Input.MouseListener.getYWorld() : (int)Input.MouseListener.getYWorld() - snap_y;

        gameObject.setPos(new Vector2f(x,y));
    }

    @Override
    public void start() {

    }
}
