package org.misterej.engine.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;
import org.misterej.engine.GameObject;

public class ImGuiLayer {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private GameObject selectedObject;

    private long window;
    public String glslVersion = "#version 130";

    private float timer = 1;
    private int fps = 0;

    public ImGuiLayer(long window)
    {
        this.window = window;
    }

    public void init()
    {
        ImGui.createContext();
        imGuiGl3.init();
        imGuiGlfw.init(window, true);
    }

    public void dispose()
    {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    private void imgui(float deltaTime)
    {
        timer += deltaTime;
        ImGui.begin("Debug info");
        if(timer > 1)
        {
            fps = (int)(1 / deltaTime);
            timer = 0;
        }
        ImGui.text(fps + " FPS");
        ImGui.end();

        ImGui.showDemoWindow();

        if(selectedObject != null)
        {
            ImGui.begin("Inspector");
            ImGui.text("inspector");
            selectedObject.imgui();
            ImGui.end();
        }
    }

    public void update(float deltaTime)
    {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        imgui(deltaTime);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }


    }
}
