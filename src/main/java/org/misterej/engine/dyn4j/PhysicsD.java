package org.misterej.engine.dyn4j;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.geometry.*;
import org.dyn4j.world.World;
import org.dyn4j.world.result.RaycastResult;
import org.misterej.engine.GameObject;
import org.misterej.engine.dyn4j.components.BoxCollider;
import org.misterej.engine.dyn4j.components.CircleCollider;
import org.misterej.engine.dyn4j.components.RigidBody2D;
import org.misterej.game.GameScene;

import java.util.List;

public class PhysicsD {
    World<Body> world = new World<Body>();

    public void add(GameObject go)
    {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if(rb == null) return;

        Body body = new Body();

        BoxCollider boxCollider = go.getComponent(BoxCollider.class);
        CircleCollider circleCollider = go.getComponent(CircleCollider.class);
        if(boxCollider != null)
        {
            body.addFixture(Geometry.createRectangle(boxCollider.getSize().x, boxCollider.getSize().y));
        } else if(circleCollider != null)
        {
            body.addFixture(Geometry.createCircle(circleCollider.getRadius()));
        }

        body.translate(new Vector2(go.transform.position.x, go.transform.position.y));
        body.setMass(rb.getMass());
        body.setUserData(go);
        rb.setRawBody(body);
        world.addBody(body);
    }

    public void update(float deltaTime)
    {
        world.update(deltaTime);
    }

    public List<RaycastResult<Body, BodyFixture>> raycast(Vector2 position,double direction, double lenght)
    {
        List<RaycastResult<Body, BodyFixture>> rz;
        Ray ray = new Ray(position, direction);
        rz = world.raycast(ray, lenght, null);
        return rz;
    }
}
