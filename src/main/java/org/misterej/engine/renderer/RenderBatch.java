package org.misterej.engine.renderer;

import org.joml.Vector4f;
import org.misterej.engine.GameObject;
import org.misterej.engine.SceneManager;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    // VERTEX
    // ======
    //  Position            Color                           UV
    //  float, float        float, float, float, float      float, float
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = (POSITION_OFFSET + POSITION_SIZE) * Float.BYTES;
    private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private Map<SpriteRenderer, Integer> spriteIndex = new HashMap<>();
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize)
    {
        shader = AssetPool.getShader("assets/shaders/default.glsl");

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE]; // 4 vertices per quad
        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start()
    {
        // Generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indicies = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        Logger.logGL(glGetError());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);
        Logger.logGL(glGetError());

        //Enable buffer Attribute Pointers
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE , GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

    }

    public void render()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }

    public void updateSprite(SpriteRenderer spr)
    {
        if(this.spriteIndex.containsKey(spr))
        {
            int i = this.spriteIndex.get(spr);
            sprites[i] = spr;
            loadVertexProperties(i);
        }
    }

    public void addSprite(SpriteRenderer spr)
    {
        // Get index and add render object
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;
        this.spriteIndex.put(spr, index);

        loadVertexProperties(index);

        if(this.numSprites >= this.maxBatchSize)
        {
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index)
    {
        SpriteRenderer sprite = this.sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor().getColor4f();
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++)
        {
            if (i == 1)
            {
                yAdd = 0.0f;
            } else if(i == 2)
            {
                xAdd = 0.0f;
            } else if (i == 3)
            {
                yAdd = 1.0f;
            }

            vertices[offset] = sprite.gameObject.transfrom.position.x + (xAdd * sprite.gameObject.transfrom.scale.x);
            vertices[offset+1] = sprite.gameObject.transfrom.position.y + (yAdd * sprite.gameObject.transfrom.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }


    }

    private int[] generateIndices()
    {
        // 6 indices pre quad, 3 per triangle
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++)
        {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;
        //Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom()
    {
        return this.hasRoom;
    }

}
