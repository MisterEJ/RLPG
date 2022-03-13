package org.misterej.engine.util;

import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.renderer.Shader;
import org.misterej.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static final Map<String, Shader> shaders = new HashMap<>();
    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String filePath)
    {
        File file = new File(filePath);
        if(!shaders.containsKey(file.getAbsolutePath()))
        {
            Shader shader = new Shader(file.getAbsolutePath());
            shader.compile();
            shaders.put(file.getAbsolutePath(), shader);
        }

        return shaders.get(file.getAbsolutePath());
    }

    public static Texture getTexture(String filePath)
    {
        File file = new File(filePath);
        if(!textures.containsKey(file.getAbsolutePath()))
        {
            Texture texture = new Texture(file.getAbsolutePath());
            textures.put(file.getAbsolutePath(), texture);
        }

        return textures.get(file.getAbsolutePath());
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
        {
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
        {
            assert false : "No sprite sheet: " + resourceName;
        }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }

}
