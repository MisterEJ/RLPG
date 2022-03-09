package org.misterej.game;


import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Rotation;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import org.dyn4j.world.result.RaycastResult;
import org.joml.Vector2f;

import org.lwjgl.glfw.GLFW;
import org.misterej.engine.*;
import org.misterej.engine.components.Script;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.dyn4j.components.RigidBody2D;

import java.util.List;


public class PlayerController extends Script {
    public PlayerController(GameObject gameObject) {
        super(gameObject);
        this.startingPosition = new Vector2f(gameObject.getPosition());
    }

    enum State
    {
        IDLE,
        ALIVE,
        DEAD
    }

    private Camera camera;
    private SpriteRenderer spr;
    private RigidBody2D rb;
    private double speed = 5;
    private double jumpImpulse = 0.8;
    private State state = State.IDLE;
    private Vector2f startingPosition;

    @Override
    public void update(float deltaTime) {


        if(state == State.ALIVE)
        {
            onGround();
            rb.getRawBody().setLinearVelocity(speed, rb.getRawBody().getLinearVelocity().y);

            if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE) && onGround())
            {
                rb.getRawBody().applyImpulse(new Vector2(0, jumpImpulse));
            }

            if(checkIsDead())
            {
                state = State.DEAD;
            }
        }

        if(state == State.IDLE)
        {
            if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE))
            {
                state = State.ALIVE;
            }
        }

        if(state == State.DEAD)
        {
            state = State.IDLE;
            Transform tr = new Transform();
            tr.translate(new Vector2(startingPosition.x, startingPosition.y));
            rb.getRawBody().setTransform(tr);
            rb.getRawBody().setLinearVelocity(new Vector2(0,0));
            System.out.println("dead");
        }


        camera.position.x = gameObject.getTransform().position.x - (camera.getViewPort().x / 2f) + (gameObject.getTransform().size.x / 2f);
        camera.position.y = gameObject.getTransform().position.y - (camera.getViewPort().y / 2f) + (gameObject.getTransform().size.y / 2f);
    }

    public boolean onGround()
    {
        List<RaycastResult<Body, BodyFixture>> rez = SceneManager.getCurrentScene().getPhysics().raycast(
                new Vector2(gameObject.transform.position.x, gameObject.transform.position.y),
                Rotation.rotation270().toRadians(),
                0.25);

        List<RaycastResult<Body, BodyFixture>> rez2 = SceneManager.getCurrentScene().getPhysics().raycast(
                new Vector2(gameObject.transform.position.x + gameObject.transform.size.x, gameObject.transform.position.y),
                Rotation.rotation270().toRadians(),
                0.25);
        if(rez != null)
        {
            for (RaycastResult<Body, BodyFixture> r : rez)
            {
                if(r.getBody().getUserData() != gameObject)
                {
                    return true;
                }
            }
        }

        if(rez2 != null)
        {
            for (RaycastResult<Body, BodyFixture> r : rez2)
            {
                if(r.getBody().getUserData() != gameObject)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIsDead()
    {
        List<RaycastResult<Body, BodyFixture>> rez = SceneManager.getCurrentScene().getPhysics().raycast(
                new Vector2(gameObject.transform.position.x, gameObject.transform.position.y),
                Rotation.rotation0().toRadians(),
                0.25);
        if(rez != null)
        {
            for (RaycastResult<Body, BodyFixture> r : rez)
            {
                if(r.getBody().getUserData() != gameObject)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        camera = SceneManager.getCurrentScene().getCamera();
        this.rb = gameObject.getComponent(RigidBody2D.class);
        spr = gameObject.getComponent(SpriteRenderer.class);
    }
}
