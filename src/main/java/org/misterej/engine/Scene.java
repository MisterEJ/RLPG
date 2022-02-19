package org.misterej.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {


    protected Renderer renderer = new Renderer();
    protected Camera camera;

    protected List<GameObject> gameObjects = new ArrayList<>();
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
            go.start();
            isRunning = true;
            this.renderer.add(go);
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
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float deltaTime);
    public abstract void init();
}
