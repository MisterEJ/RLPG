package org.misterej.engine;

import org.misterej.engine.imgui.ImGuiLayer;

import java.util.List;

public class LevelEditor extends Scene{

    private Renderer renderer = new Renderer();
    private List<GameObject> gameObjects;
    private ImGuiLayer imguilayer;

    @Override
    public void update(float deltaTime) {
        renderer.prepare();
        imguilayer.update(deltaTime);
    }

    @Override
    public void init() {
        imguilayer = new ImGuiLayer(Window.getWindow());
        imguilayer.init();
    }
}
