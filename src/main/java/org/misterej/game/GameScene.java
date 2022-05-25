package org.misterej.game;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.misterej.engine.*;
import org.misterej.engine.components.*;
import org.misterej.engine.physics2d.components.Box2DCollider;
import org.misterej.engine.physics2d.components.FloorCollider;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.engine.physics2d.enums.BodyType;
import org.misterej.engine.renderer.Color;
import org.misterej.engine.util.AssetPool;

import java.util.ArrayList;
import java.util.List;


public class GameScene extends Scene {

    private Tilemap tilemap;
    private GameObject player;

    public GameScene(String level)
    {
        super(level);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(Input.KeyboardListener.iskeyPressed(GLFW.GLFW_KEY_F1))
        {
            SceneManager.setScene(new LevelEditor(level));
        }
    }

    @Override
    public void init() {
        loadResources();

        renderer.setClearColor(new Color(31f/255f,14f/255f,28f/255f, 1));

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/DeepForestTileset.png");

        tilemap = new Tilemap(0.5f,0.5f, spriteSheet);
        tilemap.load(level);

        for(GameObject go : tilemap.getTiles())
        {
            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 80)
            {
//                // Player
                go.addComponent(new RigidBody2D());
                go.getComponent(RigidBody2D.class).setBodyType(BodyType.Dynamic);
                go.getComponent(RigidBody2D.class).setFixedRotation(true);
                go.addComponent(new Box2DCollider());
                go.getComponent(Box2DCollider.class).setHalfSize(new Vector2f(go.transform.size).div(2));
                go.getComponent(Box2DCollider.class).setOffset(new Vector2f(go.transform.size).div(2));
                Box2DCollider box2DCollider = new Box2DCollider();
                box2DCollider.setHalfSize(new Vector2f(go.transform.size.x / 2, 0.1f));
                box2DCollider.setOffset(box2DCollider.getHalfSize());
                box2DCollider.setSensor(true);
                box2DCollider.color.x = 1;
                go.addComponent(box2DCollider);

                player = go;
                player.setName("Player");
                player.transform.size.mul(2f);
                player.getComponent(SpriteRenderer.class).setZIndex(1);
            }

            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 72)
            {
                go.addComponent(new RigidBody2D());
                go.getComponent(RigidBody2D.class).setBodyType(BodyType.Static);
                go.addComponent(new Box2DCollider());
                go.getComponent(Box2DCollider.class).setHalfSize(new Vector2f(go.transform.size.x * 0.8f, go.transform.size.y/4).div(2));
                go.getComponent(Box2DCollider.class).setOffset(new Vector2f(go.transform.size.x * 0.8f, go.transform.size.y/4).div(2).add(-0.1f,0));
                go.getComponent(Box2DCollider.class).setSensor(true);
            }

            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 81)
            {
                go.addComponent(new RigidBody2D());
                go.getComponent(RigidBody2D.class).setBodyType(BodyType.Static);
                go.addComponent(new Box2DCollider());
                go.getComponent(Box2DCollider.class).setHalfSize(new Vector2f(go.transform.size.x, go.transform.size.y));
                go.getComponent(Box2DCollider.class).setOffset(new Vector2f(go.transform.size.x, go.transform.size.y).div(2));
                go.getComponent(Box2DCollider.class).setSensor(true);
            }

            List<Integer> floor = new ArrayList();
            for(int i = 0; i < 24; i++)
            {
                if(i != 15)
                    floor.add(i);
            }
            floor.add(37);
            floor.add(38);
            floor.add(39);
            floor.add(39+8);
            floor.add(38+8);
            floor.add(37+8);

            if(floor.contains(go.getComponent(SpriteRenderer.class).getSprite().getId()))
            {
                go.addComponent(new RigidBody2D());
                go.getComponent(RigidBody2D.class).setBodyType(BodyType.Static);
                go.addComponent(new FloorCollider());
            }
            this.addGameObject(go);
        }

        player.addComponent(new AnimationPlayer());
        player.addComponent(new ScriptComponent(new PlayerController(player)));
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/MiniCavalierMan.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/MiniCavalierMan.png"), 26, 26, 36, 0
        ));

        AssetPool.addSpriteSheet("assets/textures/DeepForestTileset.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/DeepForestTileset.png"), 16, 16, 80, 0
        ));
    }

}
