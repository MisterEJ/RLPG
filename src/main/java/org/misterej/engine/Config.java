package org.misterej.engine;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Config implements Serializable {

    private static Config config = new Config();
    public static boolean isFullscreen = false;
    public static int w_width = 1280;
    public static int w_height = 720;

    public static void saveConfig()
    {
        try {
            FileOutputStream fos = new FileOutputStream("config.json");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
        } catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
