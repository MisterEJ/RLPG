package org.misterej.engine;

import java.util.*;

/*
    Scene Manager
    Manages scenes
    Singleton Class
 */

public class SceneManager {

    private static SceneManager instance;
    private List<Scene> scenes = new ArrayList<>();

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
        assert getInstance().currentScene != null : "Unknown scene: " + getInstance().currentScene;
        return getInstance().currentScene;
    }

    public static <T extends Scene> void setScene(Class<T> componentClass)
    {
        for (Scene scene : getInstance().scenes)
        {
            if(componentClass.isAssignableFrom(scene.getClass()))
            {
                getInstance().currentScene = scene;
                return;
            }
        }
    }

    /**
     * Add a scene to the scene manager
     */
    public static void addScene(Scene scene)
    {
        getInstance().scenes.add(scene);

        if(getInstance().currentScene == null && getInstance().scenes.size() == 1)
        {
            getInstance().currentScene = getInstance().scenes.get(0);
        }
    }
}
