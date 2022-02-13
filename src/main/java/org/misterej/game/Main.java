package org.misterej.game;

import org.misterej.engine.Input;
import org.misterej.engine.render.Renderer;
import org.misterej.engine.Window;
import org.misterej.engine.render.Color;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    private Window window = new Window("RLPG");
    private Renderer renderer = new Renderer();

    public static void main(String[] args)
    {
        new Main().run();
    }

    public void run()
    {
        // START GAME LOOP
        loop();

        window.close();
    }

    private void loop()
    {
        while(!window.shouldClose())
        {
            renderer.prepare();

            if(Input.KeyboardListener.iskeyPressed(GLFW_KEY_Q)) renderer.setClearColor(Color.Green);

            glBegin(GL_TRIANGLES);
            glVertex2f(-0.5f, -0.5f);
            glVertex2f(0.0f, 0.5f);
            glVertex2f(0.5f, -0.5f);
            glEnd();

            window.update();
        }
    }

}
