package org.misterej.game;

import org.misterej.engine.LevelEditor;
import org.misterej.engine.Scene;
import org.misterej.engine.SceneManager;
import org.misterej.engine.Window;


public class Main {

    public static void main(String[] args)
    {
        new Main().run();
    }

    public void run()
    {
        Scene scene = new GameScene();

        SceneManager.addScene(scene);

        Window.get().run();
    }

}
