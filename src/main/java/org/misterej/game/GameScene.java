package org.misterej.game;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.dyn4j.components.BoxCollider;
import org.misterej.engine.dyn4j.components.CircleCollider;
import org.misterej.engine.dyn4j.components.Collider;
import org.misterej.engine.dyn4j.components.RigidBody2D;
import org.misterej.engine.physics2d.components.Box2DCollider;
import org.misterej.engine.physics2d.enums.BodyType;
import org.misterej.engine.renderer.DebugDraw;
import org.misterej.engine.util.AssetPool;


public class GameScene extends Scene {

    Tilemap tilemap;
    GameObject player;

    @Override
    public void update(float deltaTime) {
        this.physics2D.update(deltaTime);

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
        }

        this.renderer.render();
    }

    @Override
    public void init() {
        loadResources();

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/sprites.png");

        tilemap = new Tilemap(0.5f,0.5f, spriteSheet);
        tilemap.load("assets/levels/lvl1.csv");
        this.addTilemap(tilemap);


        for(GameObject go : gameObjects)
        {
            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 0)
            {
//                // Player
                go.addComponent(new RigidBody2D());
                go.addComponent(new BoxCollider());
                go.getComponent(RigidBody2D.class).setMass(MassType.FIXED_ANGULAR_VELOCITY);
                go.getComponent(BoxCollider.class).setSize(new Vector2(go.transform.size.x, go.transform.size.y));
//                go.addComponent(new RigidBody2D());
//                go.getComponent(RigidBody2D.class).setMass(10);
//                go.getComponent(RigidBody2D.class).setFixedRotation(true);
//                go.addComponent(new CircleCollider());
//                go.getComponent(CircleCollider.class).setRadius((go.transform.size.y / 2f));
//                go.getComponent(CircleCollider.class);
                player = go;
                player.setName("Player");
            }

            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 2)
            {
//                // Floor
                go.addComponent(new RigidBody2D());
                go.getComponent(RigidBody2D.class).setMass(MassType.INFINITE);
                go.addComponent(new BoxCollider());
                go.getComponent(BoxCollider.class).setSize(new Vector2(go.transform.size.x, go.transform.size.y));
//                go.addComponent(new Box2DCollider());
//                go.getComponent(Box2DCollider.class).setHalfSize(new Vector2f(go.transform.size));
//                go.getComponent(Box2DCollider.class).setOrigin(new Vector2f(go.transform.size).div(2));
            }
        }
        player.addComponent(new ScriptComponent(new PlayerController(player)));
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/sprites.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/sprites.png"), 16, 16, 3, 0
    ));
    }

}
