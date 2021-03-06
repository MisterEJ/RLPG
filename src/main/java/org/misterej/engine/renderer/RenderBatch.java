package org.misterej.engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.misterej.engine.GameObject;
import org.misterej.engine.Renderer;
import org.misterej.engine.SceneManager;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.util.AssetPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class RenderBatch implements Comparable<RenderBatch> {
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

    boolean rebuffer = true;
    private int zIndex;

    private Renderer renderer;

    private Shader shader;

    public RenderBatch(int maxBatchSize, int zIndex, Renderer renderer)
    {
        this.zIndex = zIndex;
        this.renderer = renderer;
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
                if(!hasTexture(sprites[i].getTexture()))
                {
                    renderer.destroyGameObject(sprites[i].gameObject);
                    renderer.add(sprites[i].gameObject);
                } else
                {
                    loadVertexProperties(i);
                    sprites[i].resetDirtyFlag();
                    rebuffer = true;
                }

                if (sprites[i].getZIndex() != this.zIndex) {
                    destroyIfExists(sprites[i].gameObject);
                    renderer.add(sprites[i].gameObject);
                    i--;
                }
            }
        }
        if(rebuffer)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
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

    public boolean destroyIfExists(GameObject go) {
        SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
        for (int i=0; i < numSprites; i++) {
            if (sprites[i] == sprite) {
                for (int j=i; j < numSprites - 1; j++) {
                    sprites[j] = sprites[j + 1];
                    sprites[j].setDirtyFlag();
                }
                numSprites--;
                return true;
            }
        }

        return false;
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

        boolean isRotated = sprite.gameObject.transform.rotation != 0.0f;
        Matrix4f transformMatrix = new Matrix4f().identity();
        if (isRotated) {
            transformMatrix.translate(sprite.gameObject.transform.position.x,
                    sprite.gameObject.transform.position.y, 0f);
            transformMatrix.rotate((float)Math.toRadians(sprite.gameObject.transform.rotation),
                    0, 0, 1);
            transformMatrix.scale(sprite.gameObject.transform.size.x,
                    sprite.gameObject.transform.size.y, 1);
        }

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

            Vector4f currentPos = new Vector4f(sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.size.x),
                    sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.size.y),
                    0, 1);
            if (isRotated) {
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMatrix);
            }

            // Position
            vertices[offset] = currentPos.x;
            vertices[offset+1] = currentPos.y;

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

    public int getZIndex()
    {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o)
    {
        return Integer.compare(getZIndex(), o.getZIndex());
    }
}
