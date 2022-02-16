package org.misterej.game;

import org.misterej.engine.Scene;
import org.misterej.engine.SceneManager;
import org.misterej.engine.Window;


public class Main {

    private Window window = new Window("RLPG");

    public static void main(String[] args)
    {
        new Main().run();
    }

    public void run()
    {
        Scene scene1 = new GameScene();

        SceneManager.addScene(scene1);

        window.run();
    }

}
