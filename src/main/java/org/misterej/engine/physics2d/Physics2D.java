package org.misterej.engine.physics2d;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.SceneManager;
import org.misterej.engine.Transform;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.physics2d.components.Box2DCollider;
import org.misterej.engine.physics2d.components.CircleCollider;
import org.misterej.engine.physics2d.components.RigidBody2D;

public class Physics2D {

    private Vec2 gravity = new Vec2(0.0f, -10.0f);
    private World world = new World(gravity);

    private float physcsTime = 0.0f;
    private final float physcsTimeStep = 1f / 60f;

    private final int velocityIterations = 8;
    private final int positionIterations = 3;

    public void add(GameObject go)
    {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if(rb != null && rb.getRawBody() == null)
        {
            Transform transform = go.getTransform();

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch(rb.getBodyType())
            {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }


            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);

            CircleCollider circleCollider;
            Box2DCollider box2DCollider;

            if((circleCollider = go.getComponent(CircleCollider.class)) != null)
            {
                addCircleCollider(rb, circleCollider);
            }
            else if( (box2DCollider = go.getComponent(Box2DCollider.class)) != null)
            {
                addBox2DCollider(rb, box2DCollider);
            }
        }
    }

    public void addBox2DCollider(RigidBody2D rb, Box2DCollider boxCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = boxCollider.getOffset();
        Vector2f origin = new Vector2f(boxCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.userData = boxCollider.gameObject;
        body.createFixture(fixtureDef);
    }

    public void destroyGameObject(GameObject go)
    {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if(rb != null)
        {
            if(rb.getRawBody() != null)
            {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void addCircleCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.userData = circleCollider.gameObject;
        body.createFixture(fixtureDef);
    }

    public World getWorld()
    {
        return world;
    }

    public void update(float deltaTime)
    {
        physcsTime += deltaTime;
        while(physcsTime >= physcsTimeStep)
        {
            physcsTime -= physcsTimeStep;
            world.step(physcsTimeStep, velocityIterations, positionIterations);
        }
    }

    public RaycastInfo raycast(GameObject requestingObject, Vector2f point1, Vector2f point2) {
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(point1.x, point1.y),
                new Vec2(point2.x, point2.y));
        return callback;
    }

//    public static boolean checkOnGround(GameObject gameObject, float innerPlayerWidth, float height) {
//        Vector2f raycastBegin = new Vector2f(gameObject.transform.position);
//        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
//        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, height);
//
//        RaycastInfo info = SceneManager.getCurrentScene().getPhysics().raycast(gameObject, raycastBegin, raycastEnd);
//
//        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
//        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
//        RaycastInfo info2 = SceneManager.getCurrentScene().getPhysics().raycast(gameObject, raycast2Begin, raycast2End);
//
//        return (info.hit && info.hitObject != null && info.hitObject.getComponent(SpriteRenderer.class).getSprite().getId() == 2) ||
//                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(SpriteRenderer.class).getSprite().getId() == 2);
//    }

}
