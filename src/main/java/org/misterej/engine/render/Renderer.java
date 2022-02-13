package org.misterej.engine.render;

import org.lwjgl.opengl.GL;
import org.misterej.engine.render.Color;

import static org.lwjgl.opengl.GL11.*;

// TODO: Renderer class
public class Renderer {

    private Color clearColor = Color.Black;

    public Renderer()
    {
        GL.createCapabilities();
    }

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
