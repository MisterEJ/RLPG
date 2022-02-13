package org.misterej.engine;

import java.util.*;

//TODO REWRITE SCENE MANAGER
// ONLY ONE SCENE SHOULD BE LOADED AT A TIME
// SCENE OBJECT SHOULD STORED IN THE SCENE_MANAGER SINGLETON

public class SceneManager {
    private static SceneManager instance;
    private Map<Integer, Scene> scenes = new HashMap<>();

    public static SceneManager getInstance() {
        if(instance == null) instance = new SceneManager();
        return instance;
    }

    private static Scene currentScene = null;

    public static Scene getCurrentScene()
    {
        assert currentScene != null : "Unknown scene: " + currentScene;
        return currentScene;
    }

    public static void setScene(int id)
    {
        if(getInstance().scenes.get(id) != null)
        {
            currentScene = getInstance().scenes.get(id);
        }
    }

    public static void addScene(Scene scene)
    {
        getInstance().scenes.put(scene.id, scene);
    }
}
