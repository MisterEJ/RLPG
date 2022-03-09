package org.misterej.engine;

import java.util.*;


public class SceneManager {

    private static SceneManager instance;

    private Scene currentScene = null;

    public static SceneManager getInstance()
    {
        if(instance == null) instance = new SceneManager();
        return instance;
    }

    /**
     * Gets the current scene
     */
    public static Scene getCurrentScene()
    {
        return getInstance().currentScene;
    }

    public static void setScene(Scene scene)
    {
        getInstance().currentScene = scene;
        scene.init();
        scene.start();
    }
}
