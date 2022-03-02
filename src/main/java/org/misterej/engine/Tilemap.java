package org.misterej.engine;

import org.joml.Vector2f;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.util.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tilemap {
    private List<GameObject> tiles;
    private SpriteSheet tileSheet;
    private int cell_width;
    private int cell_height;

    public Tilemap(int cell_width, int cell_height, SpriteSheet spritesheet)
    {
        this.cell_width = cell_width;
        this.cell_height = cell_height;
        this.tileSheet = spritesheet;
        this.tiles = new ArrayList<>();
    }

    public void load(String resourceName)
    {
        //TODO: load tilemap from file
        try
        {
            FileInputStream fis = new FileInputStream(resourceName);
            String csv = "";
            int c;
            while(true)
            {
                c = fis.read();
                if(c == -1)
                {
                    break;
                }
                csv += (char)c;
            }
            String[] chars = csv.split("[,]");
            Logger.log("Tilemap tilecount: " + chars.length / 3);
            if(chars.length % 3 == 0)
            {
                for (int i = 0; i < chars.length; i += 3)
                {
                    int x, y, index;
                    x = Integer.parseInt(chars[i]);
                    y = Integer.parseInt(chars[i+1]);
                    index = Integer.parseInt(chars[i+2]);
                    add_tile(x,y,index);
                    System.out.println(x + " " + y);
                }
            } else
            {
                assert false : "(Tilemap) csv file bad format";
            }
        } catch(Exception e)
        {
            assert false : "(Tilemap) Cannot load resource '" + resourceName + "'";
        }
    }

    public void add_tile(int x, int y, int index)
    {
        GameObject go = new GameObject("tile@x" + x + "y" + y + "i" + index, new Transform(new Vector2f(x * cell_width,y*cell_height), new Vector2f(cell_width, cell_height)));
        go.addComponent(new SpriteRenderer(tileSheet.getSprite(index)));
        tiles.add(go);
    }

    public List<GameObject> getTiles()
    {
        return this.tiles;
    }

    public int getCellHeight() {
        return cell_height;
    }

    public int getCellWidth()
    {
        return cell_width;
    }
}