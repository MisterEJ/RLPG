package org.misterej.engine;

import org.joml.Vector2f;
import org.misterej.engine.dyn4j.PhysicsD;
import org.misterej.engine.physics2d.Physics2D;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.game.GameScene;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {


    protected Renderer renderer = new Renderer();
    protected Camera camera = new Camera(new Vector2f());

    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Physics2D physics2D = new Physics2D();
    private boolean isRunning = false;
    
    public int id;
    public static int _id = 0;
    public Scene()
    {
        id = _id;
        _id++;
    }

    /**
     * Start the scene
     * Calls the start method for the objects in the scene
     */
    public void start()
    {
        for(GameObject go : gameObjects)
        {
            this.renderer.add(go);
            this.physics2D.add(go);
            go.start();
        }
        isRunning = true;
    }

    /**
     * Returns the camera
     */
    public Camera getCamera()
    {
        return this.camera;
    }

    /**
     * Add GameObject to the scene
     * @param go GameObject
     */
    public void addGameObject(GameObject go)
    {
        if(!isRunning)
        {
            gameObjects.add(go);
        }
        else if(isRunning)
        {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
    }

    public void removeGameObject(GameObject go)
    {
        renderer.destroyGameObject(go);
        //physics2D.destroyGameObject(go);
        this.gameObjects.remove(go);
    }

    public void addTilemap(Tilemap tilemap)
    {
        for (GameObject go : tilemap.getTiles())
        {
            if(!gameObjects.contains(go))
            {
                addGameObject(go);
            }
        }
    }

    public GameObject getGameObjectByID(int id)
    {
        for(GameObject go : gameObjects)
        {
            if(go.getId() == id)
            {
                return go;
            }
        }

        return null;
    }

    public GameObject getGameObjectByPos(Vector2f position)
    {
        for(GameObject gameObject : gameObjects)
        {
            if(gameObject.getTransform().position.x == position.x && gameObject.getTransform().position.y == position.y)
            {
                System.out.println("found");
                return gameObject;
            }
        }
        return null;
    }

    public GameObject getGameObjectByPos(Vector2f position, GameObject[] exclude)
    {
        for(GameObject gameObject : gameObjects)
        {
            if(gameObject.getTransform().position.x == position.x && gameObject.getTransform().position.y == position.y)
            {
                for(GameObject go : exclude)
                {
                    if(!(go == gameObject))
                    {
                        System.out.println("found");
                        return gameObject;
                    }
                }
            }
        }
        return null;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
    public Physics2D getPhysics()
    {
        return physics2D;
    }
    public Renderer getRenderer()
    {
        return renderer;
    }

    public void imgui(){};

    public abstract void update(float deltaTime);
    public abstract void init();
}
