package org.misterej.engine;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.misterej.engine.physics2d.Physics2D;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.game.GameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera = new Camera(new Vector2f(0,0));
    protected String level;

    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Physics2D physics2D = new Physics2D();
    private boolean isRunning = false;
    private List<GameObject> gameObjectsToRemove = new ArrayList<>();
    private List<GameObject> gameObjectsToAdd = new ArrayList<>();
    
    public int id;
    public static int _id = 0;
    public Scene(String level)
    {
        this.level = level;
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

    public void removeAllObjects()
    {
        for (GameObject gameObject : gameObjects)
        {
            removeGameObject(gameObject);
        }
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
            gameObjectsToAdd.add(go);
        }
    }

    private void add_object(GameObject go)
    {
        gameObjects.add(go);
        go.start();
        this.renderer.add(go);
        this.physics2D.add(go);
    }

    private void remove_object(GameObject go)
    {
        renderer.destroyGameObject(go);
        physics2D.destroyGameObject(go);
        this.gameObjects.remove(go);
    }

    public void removeGameObject(GameObject go)
    {
        if(!gameObjectsToRemove.contains(go)) gameObjectsToRemove.add(go);
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
                return gameObject;
            }
        }
        return null;
    }

    public GameObject getGameObjectByName(String name)
    {
        for(GameObject gameObject : gameObjects)
        {
            if(gameObject.getName() == name)
            {
                return gameObject;
            }
        }
        return null;
    }

    public GameObject getGameObjectByPos(Vector2f position, GameObject[] exclude)
    {
        final float EPSILON = 0.00001f;
        for(GameObject gameObject : gameObjects)
        {
            if(Math.abs(gameObject.getTransform().position.x - position.x) < EPSILON && Math.abs(gameObject.getTransform().position.y - position.y) < EPSILON)
            {
                for(GameObject go : exclude)
                {
                    if(!(go == gameObject))
                    {
                        return gameObject;
                    }
                }
            }
        }
        return null;
    }

    public String getLevel()
    {
        return level;
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

    public void update(float deltaTime)
    {
        for(GameObject go : gameObjectsToRemove)
        {
            remove_object(go);
            System.out.println(go.getName());
        }
        if (gameObjectsToRemove.size() != 0) gameObjectsToRemove.clear();

        for(GameObject go : gameObjectsToAdd)
        {
            add_object(go);
        }
        if (gameObjectsToAdd.size() != 0) gameObjectsToAdd.clear();

        for(GameObject go : gameObjects)
        {
            go.update(deltaTime);
        }

        this.physics2D.update(deltaTime);
        this.renderer.render();
    }
    public abstract void init();
}
