package org.misterej.game;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import org.lwjgl.glfw.GLFW;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.physics2d.Physics2D;
import org.misterej.engine.physics2d.RaycastInfo;
import org.misterej.engine.physics2d.components.FloorCollider;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.engine.renderer.DebugDraw;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.JMath;

import java.awt.*;


public class PlayerController extends Script {
    public PlayerController(GameObject gameObject) {
        super(gameObject);
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
    private AnimationPlayer animationPlayer;
    private float speed = 3f;
    private float jumpImpulse = 7f;
    private State state = State.IDLE;
    private Vector2f startingPosition;
    private Vector2f lastPosition;
    private Vector2f positionDelta = new Vector2f();
    boolean canJump = false;
    private int contacts = 0;

    @Override
    public void update(float deltaTime) {
        if(contacts > 0) canJump = true;
            else canJump = false;
        animate();

        if(state == State.IDLE)
        {
            if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE))
            {
                state = State.ALIVE;
                canJump = false;
            }
        } else if(state == State.ALIVE)
        {
            rb.getRawBody().setLinearVelocity(new Vec2(speed, rb.getRawBody().getLinearVelocity().y));

            if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_SPACE) && canJump)
            {
                rb.getRawBody().setLinearVelocity(new Vec2(speed, jumpImpulse));
                canJump = false;
            }
        } else if(state == State.DEAD)
        {
            rb.getRawBody().setLinearVelocity(new Vec2(0,0));
            if(!animationPlayer.isPlaying())
            {
                if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE))
                {
                    rb.getRawBody().setTransform(new Vec2(startingPosition.x, startingPosition.y), 0);
                    state = State.IDLE;
                }
            }
        }

        camera.position.x = gameObject.getTransform().position.x - (camera.getViewPort().x / 2f) + (gameObject.getTransform().size.x / 2f);

        positionDelta.x = gameObject.transform.position.x - lastPosition.x;
        positionDelta.y = gameObject.transform.position.y - lastPosition.y;
        lastPosition.x = gameObject.transform.position.x;
        lastPosition.y = gameObject.transform.position.y;
    }

    private void animate()
    {
        if(state == State.ALIVE)
        {
            switch (Float.compare(positionDelta.y, 0f))
            {
                case 0:
                    if(canJump) animationPlayer.setAnimation("RUN", true);
                    break;
                case 1:
                    animationPlayer.setAnimation("JUMPING", false);
                    break;
                case -1:
                    animationPlayer.setAnimation("FALLING", false);
                    break;
            }
        }
        else if(state == State.IDLE)
        {
            animationPlayer.setAnimation("IDLE", true);
        }
        else if(state == State.DEAD)
        {
            animationPlayer.setAnimation("DIE", true);
        }
    }

    public void setUpAnimations()
    {
        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/MiniCavalierMan.png");
        Animation animationRun = new Animation("RUN", spriteSheet, true);
        animationRun.addFrames(new int[]{6,7,8,9,10,11});
        animationRun.setSpeed(12);

        Animation animationIdle = new Animation("IDLE", spriteSheet, true);
        animationIdle.addFrames(new int[]{0,1,2,3});
        animationIdle.setSpeed(12);

        Animation animationDie = new Animation("DIE", spriteSheet, false);
        animationDie.addFrames(new int[]{30,31,32,33,34,35});
        animationDie.setSpeed(12);

        Animation animationJumping = new Animation("JUMPING", spriteSheet, true);
        animationJumping.addFrames(new int[]{24});
        animationJumping.setSpeed(12);

        Animation animationFalling = new Animation("FALLING", spriteSheet, true);
        animationFalling.addFrames(new int[]{25});
        animationFalling.setSpeed(12);

        Animation animationAttack = new Animation("ATTACK", spriteSheet, false);
        animationAttack.addFrames(new int[]{12,13,14,15,16,17});
        animationAttack.setSpeed(12);

        animationPlayer.addAnimation(animationIdle);
        animationPlayer.addAnimation(animationRun);
        animationPlayer.addAnimation(animationDie);
        animationPlayer.addAnimation(animationJumping);
        animationPlayer.addAnimation(animationFalling);
        animationPlayer.addAnimation(animationAttack);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f normal)
    {
        if(JMath.compare(normal, new Vector2f(1, 0)) && obj.getComponent(FloorCollider.class) != null)
        {
            state = State.DEAD;
        }
        if(obj.getComponent(SpriteRenderer.class).getSprite().getId() == 72)
        {
            state = State.DEAD;
        }

        if(contact.getFixtureB().isSensor())
        {
            GameObject go = (GameObject) contact.getFixtureA().getUserData();
            if(go.getComponent(FloorCollider.class) != null)
            {
                contacts++;
            }
        }
    }

    @Override
    public void endCollision(GameObject obj, Contact contact, Vector2f normal)
    {
        if(contact.getFixtureB().isSensor())
        {
            GameObject go = (GameObject) contact.getFixtureA().getUserData();
            if(go.getComponent(FloorCollider.class) != null)
            {
                contacts--;
            }
        }
    }

    @Override
    public void start() {
        camera = SceneManager.getCurrentScene().getCamera();
        startingPosition = new Vector2f(gameObject.getPosition());
        lastPosition = new Vector2f(startingPosition);

        camera.position.y = gameObject.getTransform().position.y - (camera.getViewPort().y / 2f) + (gameObject.getTransform().size.y / 2f);
        rb = gameObject.getComponent(RigidBody2D.class);
        rb.getRawBody().setGravityScale(3f);
        spr = gameObject.getComponent(SpriteRenderer.class);

        animationPlayer = gameObject.getComponent(AnimationPlayer.class);
        setUpAnimations();
    }
}
