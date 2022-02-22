package org.misterej.engine.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.misterej.engine.GameObject;
import org.misterej.engine.SceneManager;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    // VERTEX
    // ======
    //  Position            Color                           UV                      texID
    //  float, float        float, float, float, float      float, float            float

    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET  + (POSITION_SIZE * Float.BYTES);
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + (COLOR_SIZE * Float.BYTES);
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + (TEX_COORDS_SIZE * Float.BYTES);

    private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private Map<SpriteRenderer, Integer> spriteIndex = new HashMap<>();

    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private List<Texture> textures;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private int vaoID, vboID;
    private int maxBatchSize;
    private boolean rebuffer = true;

    private Shader shader;

    public RenderBatch(int maxBatchSize)
    {
        shader = AssetPool.getShader("assets/shaders/default.glsl");

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE]; // 4 vertices per quad
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
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
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);

        //Enable buffer Attribute Pointers
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE , GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT,false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT,false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void render()
    {
        updateSprite();
        shader.use();
        shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        // Bind Textures
        for(int i = 0; i < textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);


        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);


        for (Texture texture : textures) {
            texture.unbind();
        }
        glBindVertexArray(0);
        shader.detach();
    }

    public void updateSprite()
    {
        for(int i = 0; i < numSprites; i++)
        {
            if(sprites[i].isDirty())
            {
                loadVertexProperties(i);
                sprites[i].resetDirtyFlag();
                rebuffer = true;
            }
        }
        if(rebuffer)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            rebuffer = false;
        }
    }

    public void addSprite(SpriteRenderer spr)
    {
        // Get index and add render object
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;
        this.spriteIndex.put(spr, index);

        if(spr.getTexture() != null)
        {
            if(!textures.contains(spr.getTexture()))
            {
                textures.add(spr.getTexture());
            }
        }

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

        int texID = 0;
        if(sprite.getTexture() != null)
        {
            for(int i = 0; i < textures.size(); i++)
            {
                if(textures.get(i) == sprite.getTexture())
                {
                    texID = i + 1;
                    break;
                }
            }
        }

        Vector2f[] texCorrds = sprite.getTexCoords();

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

            // Position
            vertices[offset] = sprite.gameObject.getTransform().position.x + (xAdd * sprite.gameObject.getTransform().size.x * sprite.gameObject.getTransform().scale.x);
            vertices[offset+1] = sprite.gameObject.getTransform().position.y + (yAdd * sprite.gameObject.getTransform().size.y * sprite.gameObject.getTransform().scale.y);

            // Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // UV
            vertices[offset + 6] = texCorrds[i].x;
            vertices[offset + 7] = texCorrds[i].y;

            // TEX_ID
            vertices[offset + 8] = texID;

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

    public boolean hasTextureRoom()
    {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture texture)
    {
        return this.textures.contains(texture);
    }

}
