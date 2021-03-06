package org.misterej.engine;

import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.renderer.Color;
import org.misterej.engine.renderer.RenderBatch;
import org.misterej.engine.renderer.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

// TODO: Renderer class
public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;

    private Color clearColor = new Color(0.5f, 0.5f, 0.5f, 1);
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
            if(batch.hasRoom() && batch.getZIndex() == spr.getZIndex()) {
                Texture texture = spr.getTexture();

                if (texture == null || batch.hasTexture(texture) || batch.hasTextureRoom())
                {
                    batch.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }

        if(!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spr.getZIndex(), SceneManager.getCurrentScene().getRenderer());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
            Collections.sort(batches);
        }
    }

    public void destroyGameObject(GameObject go) {
        if (go.getComponent(SpriteRenderer.class) == null) return;
        for (RenderBatch batch : batches) {
            if (batch.destroyIfExists(go)) {
                return;
            }
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
