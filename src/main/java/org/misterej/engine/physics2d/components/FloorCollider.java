package org.misterej.engine.physics2d.components;

import org.joml.Vector2f;
import org.misterej.engine.Config;
import org.misterej.engine.renderer.DebugDraw;

public class FloorCollider extends Collider{

    @Override
    public void update(float deltaTime) {
        if(Config.drawColliders)
            DebugDraw.addBox2D(new Vector2f(gameObject.getPosition()).add(new Vector2f(gameObject.transform.size).div(2)), gameObject.transform.size, 0);
    }

    @Override
    public void start() {

    }
}
