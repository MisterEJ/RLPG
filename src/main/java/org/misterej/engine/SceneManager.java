package org.misterej.engine;

import java.util.*;


public class SceneManager {

    private static Scene currentScene = null;

    public static Scene getCurrentScene()
    {
        return currentScene;
    }

    public static void setScene(Scene scene)
    {
        currentScene = scene;
        scene.init();
        scene.start();
    }

    public static void startScene()
    {
        currentScene.start();
    }

    public static void updateScene(float deltaTime)
    {
        currentScene.update(deltaTime);
    }
}
