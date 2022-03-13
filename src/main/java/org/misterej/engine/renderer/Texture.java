package org.misterej.engine.renderer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.misterej.engine.util.Logger;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageWrite.*;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private String filePath;
    private int texID;
    private int width, height;

    public Texture(String filePath)
    {
        this.filePath = filePath;

        // Generate Texture
        texID = glGenTextures();
        bind();

        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // Texture stretching set to nearest
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // Texture shrinking set to nearest
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        this.width = width.get(0);
        this.height = height.get(0);
        Logger.log(filePath + " Channels: " + channels.get(0) + " Width: " + this.width + " Height: " + this.height);

        if (image != null)
        {
            if(channels.get(0) == 4)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            else if (channels.get(0) == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            else
                assert false : "ERROR: (Texture) unknown format: " + filePath;

            Logger.logGL(glGetError());
        }
        else
        {
            assert false: "ERROR:(Texture) Could not load image '" + filePath + "'";
        }

        stbi_image_free(image);
        unbind();

    }

    public Vector2f getSize()
    {
        return new Vector2f(this.width, this.height);
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTexID()
    {
        return texID;
    }
}
