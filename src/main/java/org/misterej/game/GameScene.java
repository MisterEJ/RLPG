package org.misterej.game;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
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

    private Tilemap tilemap;
    private GameObject player;

    private final String level;

    public GameScene(String level)
    {
        this.level = level;
    }

    @Override
    public void update(float deltaTime) {

        for(GameObject go : this.gameObjects)
        {
            go.update(deltaTime);
        }

        if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_F1))
        {
            SceneManager.setScene(new LevelEditor(level));
        }

        this.physics2D.update(deltaTime);
        this.renderer.render();
    }

    @Override
    public void init() {
        loadResources();

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/sprites.png");

        tilemap = new Tilemap(0.5f,0.5f, spriteSheet);
        tilemap.load(level);
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
