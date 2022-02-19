package org.misterej.engine;

import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.renderer.Color;
import org.misterej.engine.renderer.RenderBatch;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

// TODO: Renderer class
public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;

    private Color clearColor = Color.Green;
    private List<RenderBatch> batches;

    public Renderer()
    {
        this.batches = new ArrayList<>();
    }

    public void prepare()
    {
        glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void add(GameObject go)
    {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null)
        {
            add(spr);
        }
    }

    public void add(SpriteRenderer spr)
    {
        boolean added = false;
        for(RenderBatch batch : batches)
        {
            if(batch.hasRoom())
            {
                batch.addSprite(spr);
                added = true;
                break;
            }
        }

        if(!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
        }
    }

    public void updateSprite(GameObject gameObject)
    {
        for(RenderBatch batch : batches)
        {
            batch.updateSprite(gameObject);
        }
    }


    public void render()
    {
        for (RenderBatch batch : batches)
        {
            batch.render();
        }
    }

    public void setClearColor(Color c)
    {
        this.clearColor = c;
    }

}
