package org.misterej.engine.util;

import org.misterej.engine.renderer.Shader;
import org.misterej.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private Map<String, Shader> shaders;
    private Map<String, Texture> textures;

    private static AssetPool instance;

    private AssetPool()
    {
        this.shaders = new HashMap<>();
        this.textures = new HashMap<>();
    }


    public static AssetPool getInstance() {
        if(instance == null) instance = new AssetPool();
        return instance;
    }

    public static Shader getShader(String filePath)
    {
        File file = new File(filePath);
        if(!getInstance().shaders.containsKey(file.getAbsolutePath()))
        {
            Shader shader = new Shader(file.getAbsolutePath());
            shader.compile();
            getInstance().shaders.put(file.getAbsolutePath(), shader);
        }

        return getInstance().shaders.get(file.getAbsolutePath());
    }

    public static Texture getTexture(String filePath)
    {
        File file = new File(filePath);
        if(!getInstance().textures.containsKey(file.getAbsolutePath()))
        {
            Texture texture = new Texture(file.getAbsolutePath());
            getInstance().textures.put(file.getAbsolutePath(), texture);
        }

        return getInstance().textures.get(file.getAbsolutePath());
    }

}
