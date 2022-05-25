package org.misterej.game.CODE;

import imgui.ImGui;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.Scene;
import org.misterej.engine.Tilemap;
import org.misterej.engine.components.ScriptComponent;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.engine.physics2d.components.Box2DCollider;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.engine.physics2d.enums.BodyType;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.renderer.Color;
import org.misterej.game.CODE.Enums.EventType;
import org.misterej.game.CODE.Events.Event;
import org.misterej.game.CODE.Events.MessageEvent;
import org.misterej.game.CODE.Events.ResetEvent;
import org.misterej.game.CODE.chat.Client;
import org.misterej.game.CODE.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeScene extends Scene implements Observer {

    public List<String> levels = Arrays.asList("assets/levels/cod1.csv","assets/levels/cod2.csv", "assets/levels/cod3.csv");
    private Tilemap tilemap;
    private String tilesetPath = "assets/textures/codesprites.png";
    private GameObject player;
    private List<String> messages = new ArrayList<>();

    private Server server;
    private Client client;

    public CodeScene(String level)
    {
        super(level);
        EventManager.add(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void init() {
        loadResources();
        renderer.setClearColor(Color.Black);
        loadLevel(level);
    }

    public void loadLevel(String level)
    {
        player = null;
        this.level = level;
        tilemap = new Tilemap(0.5f,0.5f, AssetPool.getSpriteSheet(tilesetPath));
        removeAllObjects();
        tilemap.load(level);
        addTilemap(tilemap);
        for(GameObject go : tilemap.getTiles())
        {
            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 2)
            {
                player = go;
                player.addComponent(new ScriptComponent(new Controller(player)));
                player.getComponent(SpriteRenderer.class).setZIndex(2);
                player.addComponent(new RigidBody2D());
                player.getComponent(RigidBody2D.class).setBodyType(BodyType.Static);
                Box2DCollider collider = new Box2DCollider();
                collider.setSensor(true);
                collider.setHalfSize(new Vector2f(player.getTransform().size).div(2));
            }
            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 12)
            {
                go.setName("Princess");
            }
            if(go.getComponent(SpriteRenderer.class).getSprite().getId() == 21)
            {
                go.setName("Enemy");
            }
        }
    }

    public void change_level()
    {
        int index = levels.indexOf(level) + 1;
        if(index == -1 || index >= levels.size()) index = levels.size() - 1;
        loadLevel(levels.get(index));
    }

    ImString code = new ImString(5000);
    ImString nickname = new ImString(16);
    ImString message = new ImString(64);
    @Override
    public void imgui()
    {
        ImGui.begin("Code");

        ImGui.inputTextMultiline("", code, 400,400);
        ImGui.sameLine();
        ImGui.text("Commands to move the player:\n" +
                "player.move() -- move in the direction the player is facing.\n\n" +
                "player.look(DIR.DIRECTION) -- look at a specific direction:\n" +
                "   DIR.LEFT, DIR.RIGHT, DIR.UP, DIR.DOWN\n\n" +
                "player.pickup() -- Pickup Item form the ground.\n\n" +
                "player.drop() -- Drop Item on ground.\n\n" +
                "player.use() -- Use Item.\n\n");

        if(ImGui.button("Exec"))
        {
            ((Controller)player.getComponent(ScriptComponent.class).getScript()).exec(code.get());
        }
        ImGui.sameLine();
        if (ImGui.button("Reset"))
        {
            EventManager.notify(new ResetEvent());
        }

        ImGui.end();

        ImGui.begin("CHAT");

        ImGui.pushID("nickname");
        ImGui.inputText("username", nickname);
        ImGui.popID();
        ImGui.sameLine();

        if(ImGui.button("Connect"))
        {
            if(!nickname.get().equals("")) ConnectToServer(nickname.get());
        }

        ImGui.sameLine();
        if(ImGui.button("Start server"))
        {
            StartServer();
        }

        ImGui.pushID("message");
        ImGui.inputText("message", message);
        ImGui.popID();

        ImGui.sameLine();
        if(ImGui.button("Send"))
        {
            if(!message.get().equals("")) SendMessage(message.get());
            message.set("");
        }

        ImGui.beginChild("Messages");
        synchronized (messages)
        {
            for(String msg : messages)
            {
                ImGui.pushID(messages.indexOf(message));
                ImGui.text(msg);
                ImGui.popID();
            }
        }
        ImGui.endChild();

        ImGui.end();
    }

    private void StartServer()
    {
        if(server == null)
        {
            try {
                server = new Server(new ServerSocket(5000));
                server.Start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ConnectToServer(String username)
    {
        try {
            client = new Client(new Socket("localhost", 5000), username);
            client.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SendMessage(String message)
    {
        if(client != null)
        {
            client.sendMessage(message);
        }
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/MiniCavalierMan.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/MiniCavalierMan.png"), 26, 26, 36, 0
        ));

        AssetPool.addSpriteSheet("assets/textures/doggoanimated.png", new SpriteSheet(
                AssetPool.getTexture("assets/textures/doggoanimated.png"), 16, 16, 32, 0
        ));

        AssetPool.addSpriteSheet(tilesetPath, new SpriteSheet(
                AssetPool.getTexture(tilesetPath), 16, 16, 24, 0
        ));
    }

    @Override
    public void onNotify(Event event) {
        if(event.getType() == EventType.MESSAGE)
        {
            messages.add(((MessageEvent)event).message);
        }
    }
}
