package org.misterej.engine;

import imgui.ImGui;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.misterej.engine.components.ScriptComponent;
import org.misterej.engine.components.Sprite;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.imgui.ImGuiLayer;
import org.misterej.engine.renderer.DebugDraw;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.scripts.TilePosScript;
import org.misterej.game.GameScene;

import java.util.ArrayList;

public class LevelEditor extends Scene{

    private Tilemap tilemap;

    private int selectedSpriteID = -1;
    private GameObject selectionObject;
    private GameObject selectedObject;
    private String levelName;

    public LevelEditor(String level)
    {
        this.levelName = level;
    }

    public LevelEditor(){}

    @Override
    public void update(float deltaTime) {
        for(GameObject go : gameObjects)
        {
            go.update(deltaTime);
        }
        renderer.render();
        input();

        if(selectedSpriteID != -1)
        {
            SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/sprites.png");
            if(selectionObject != null)
            {
                selectionObject.getComponent(SpriteRenderer.class).setSprite(spriteSheet.getSprite(selectedSpriteID));
            }
        }
        else
        {
            if(selectionObject != null)
            {
                selectionObject.getComponent(SpriteRenderer.class).setSprite(new Sprite(AssetPool.getTexture("assets/textures/selection.png")));
            }
        }

        if(selectedObject != null)
        {
            selectedObject.setPos(selectionObject.getTransform().position);
        }

    }

    @Override
    public void init() {

        loadResources();

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/sprites.png");
        tilemap = new Tilemap(1f,1f, spriteSheet);

        if(levelName != null)
        {
            tilemap.load(levelName);
            this.addTilemap(tilemap);
        }

        //DebugLines
        for(int i = -200; i < 200; i++)
        {
            DebugDraw.addLine2D(new Vector2f(-100000,i * tilemap.getCellHeight()), new Vector2f(100000, i * tilemap.getCellHeight()));
            DebugDraw.addLine2D(new Vector2f(i * tilemap.getCellWidth(),100000), new Vector2f(i * tilemap.getCellWidth(), -100000));
        }

        GameObject tile = new GameObject("Tile", new Transform(new Vector2f(0,0), new Vector2f(1,1)));
        tile.addComponent(new ScriptComponent(new TilePosScript(tile, 1, 1)));
        tile.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/textures/selection.png"))));
        selectionObject = tile;
        this.addGameObject(tile);
    }

    ImString filename = new ImString();
    ImString filename2 = new ImString();
    ImString level = new ImString();
    @Override
    public void imgui()
    {
        ImGui.begin("Runtime");
        ImGui.inputText("Level to play:", level);
        if(ImGui.button("Play"))
        {
            if(!level.get().isEmpty())
            {
                String filepath = "assets/levels/";
                if(!level.get().endsWith(".csv"))
                {
                    filepath += level.get() + ".csv";
                }
                else
                {
                    filepath += level.get();
                }

                SceneManager.setScene(new GameScene(filepath));
                DebugDraw.removeAllLines();
            }
        }
        ImGui.end();

        ImGui.begin("Info");
        for(GameObject gameObject : gameObjects)
        {
            ImGui.text(gameObject.getId() + ": x" + gameObject.getTransform().position.x + " y" + gameObject.getTransform().position.x);
        }
        ImGui.end();

        ImGui.begin("Controls");
        ImGui.inputText("Filename", filename);
        if(ImGui.button("Save Tilemap"))
        {
            if(!filename.get().equals(""))
            {
                tilemap.save(filename.get());
            }
        }

        ImGui.inputText("load filename", filename2);
        if(ImGui.button("Load Tilemap"))
        {
            if(!filename2.get().isEmpty())
            {
                tilemap.removeAllTiles();

                String filepath = "assets/levels/";
                if(!filename2.get().endsWith(".csv"))
                {
                    filepath += filename2.get() + ".csv";
                }
                else
                {
                    filepath += filename2.get();
                }

                tilemap.load(filepath);
                this.addTilemap(tilemap);
            }
        }

        if(ImGui.button("New Tilemap"))
        {
            tilemap.removeAllTiles();
        }

        ImGui.end();

        float scale = 4;
        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/sprites.png");

        ImGui.begin("Tiles");

        for(int i = 0; i < spriteSheet.getSprites().size(); i++)
        {
            Sprite sprite = spriteSheet.getSprite(i);
            Vector2f[] UV = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(sprite.getTexID(), sprite.getWidth() * scale, sprite.getHeight() * scale, UV[0].x, UV[0].y, UV[2].x, UV[2].y))
            {
                selectedSpriteID = i;
            }
            ImGui.popID();
            ImGui.sameLine();
        }

        ImGui.end();
    }

    private void input()
    {
        // Drag camera
        if(Input.MouseListener.isMouseButtonDown(2))
        {
            camera.position.x += Input.MouseListener.getDx() * 0.01;
            camera.position.y -= Input.MouseListener.getDy() * 0.01;
        }

        // Zoom
        if(Input.KeyboardListener.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
        {
            Vector2f view = camera.getViewPort();
            view.x -= Input.MouseListener.getScrollY() * 20;
            view.y -= (Input.MouseListener.getScrollY() / camera.ASPECT) * 20;
            camera.setViewPort(view);
        }

        // Select Tile
        if(Input.MouseListener.isMouseButtonPressed(0) && selectedSpriteID == -1)
        {
            if(selectedObject == null)
            {
                selectedObject = getGameObjectByPos(selectionObject.getTransform().position, new GameObject[]{selectionObject});
            }
            else
            {
                selectedObject = null;
            }
        }

        // Place Tile
        if(Input.MouseListener.isMouseButtonPressed(0) && selectedSpriteID != -1)
        {
            tilemap.add_tile(selectionObject.getTransform().position.x, selectionObject.getTransform().position.y, selectedSpriteID);
            this.addTilemap(tilemap);
        }

        if(Input.MouseListener.isMouseButtonPressed(1) && selectedSpriteID != -1)
        {
            selectedSpriteID = -1;
        }

        if(Input.MouseListener.isMouseButtonPressed(1) && selectedObject != null)
        {
            tilemap.remove_tile(selectedObject);
            selectedObject = null;
        }
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/sprites.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/sprites.png"), 16, 16, 3, 0
        ));
    }
}
