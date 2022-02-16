package org.misterej.engine;

import org.misterej.engine.renderer.Color;
import org.misterej.engine.renderer.RenderBatch;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

// TODO: Renderer class
public class Renderer {

    private Color clearColor = Color.White;
    private List<RenderBatch> batches = new ArrayList<RenderBatch>();

    public void prepare()
    {
        glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }


    /**
     * Render the batches
     */
    public void render()
    {

    }

    public void setClearColor(Color c)
    {
        this.clearColor = c;
    }

}
