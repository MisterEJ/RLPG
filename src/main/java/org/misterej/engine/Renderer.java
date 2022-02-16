package org.misterej.engine;

import org.misterej.engine.renderer.Color;

import static org.lwjgl.opengl.GL11.*;

// TODO: Renderer class
public class Renderer {

    private Color clearColor = Color.White;

    public void prepare()
    {
        glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render()
    {

    }

    public void setClearColor(Color c)
    {
        this.clearColor = c;
    }

}
