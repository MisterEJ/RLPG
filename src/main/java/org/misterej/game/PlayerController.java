package org.misterej.game;


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import org.lwjgl.glfw.GLFW;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.physics2d.Physics2D;
import org.misterej.engine.physics2d.RaycastInfo;
import org.misterej.engine.physics2d.components.FloorCollider;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.JMath;
import org.misterej.engine.util.Timer;


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
    private AnimationPlayer animationPlayer;
    private float speed = 4f;
    private float jumpImpulse = 6.5f;
    private State state = State.IDLE;
    private Vector2f startingPosition;
    private boolean canJump = true;

    @Override
    public void update(float deltaTime) {

        if(state == State.IDLE)
        {
            animationPlayer.setAnimation("IDLE");
            if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE))
            {
                state = State.ALIVE;
                canJump = false;
            }
        }

        if(state == State.ALIVE)
        {
            animationPlayer.setAnimation("RUN");
            rb.getRawBody().setLinearVelocity(new Vec2(speed, rb.getRawBody().getLinearVelocity().y));

            if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_SPACE) && canJump)
            {
                rb.getRawBody().setLinearVelocity(new Vec2(speed, jumpImpulse));
                canJump = false;
            }
        }

        if(state == State.DEAD)
        {
            animationPlayer.setAnimation("DIE");
            if(!animationPlayer.isPlaying())
            {
                if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_SPACE))
                {
                    rb.getRawBody().setLinearVelocity(new Vec2(0,0));
                    rb.getRawBody().setTransform(new Vec2(startingPosition.x, startingPosition.y), 0);
                    state = State.IDLE;
                }
            }
        }


        camera.position.x = gameObject.getTransform().position.x - (camera.getViewPort().x / 2f) + (gameObject.getTransform().size.x / 2f);
        camera.position.y = gameObject.getTransform().position.y - (camera.getViewPort().y / 2f) + (gameObject.getTransform().size.y / 2f);
    }

    public boolean checkIsDead()
    {
        return false;
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

        animationPlayer.addAnimation(animationIdle);
        animationPlayer.addAnimation(animationRun);
        animationPlayer.addAnimation(animationDie);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f normal)
    {
        if(JMath.compare(normal, new Vector2f(0,-1)) && obj.getComponent(FloorCollider.class) != null)
        {
            canJump = true;
        }
        if(JMath.compare(normal, new Vector2f(1, 0)) && obj.getComponent(FloorCollider.class) != null)
        {
            state = State.DEAD;
        }
    }

    @Override
    public void start() {
        camera = SceneManager.getCurrentScene().getCamera();
        rb = gameObject.getComponent(RigidBody2D.class);
        rb.getRawBody().setGravityScale(3f);
        spr = gameObject.getComponent(SpriteRenderer.class);

        animationPlayer = gameObject.getComponent(AnimationPlayer.class);
        setUpAnimations();
    }
}
