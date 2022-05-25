package org.misterej.engine;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.misterej.engine.imgui.ImGuiLayer;

import org.misterej.engine.renderer.DebugDraw;
import org.misterej.engine.util.Logger;
import org.misterej.engine.util.Time;
import org.misterej.game.CODE.CodeScene;


import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL33.*;

public class Window {

    public static volatile boolean shoudlClose = false;

    private Window(String name)
    {
        this.title = name;
    }
    private ImGuiLayer imguilayer;

    private static Window instance = new Window("RLPG");

    private long window;
    public final String title;

    public static Window get()
    {
        return instance;
    }

    private void init()
    {
        // ERROR CALLBACK SETUP
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // CONFIGURE GLFW and CREATE THE WINDOW
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(1280, 720, title, 0, 0);
        if(window == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        // SET CALLBACKS

        glfwSetCursorPosCallback(window, Input.MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, Input.MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, Input.MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(window, Input.KeyboardListener::keyCallback);
        glfwSetWindowSizeCallback(window, (long window, int width, int height) -> {
            Config.w_height = height;
            Config.w_width = width;
            glViewport(0,0, width, height);
        });

        // Center the window
        try (MemoryStack stack = stackPush())
        {
            // glfw returns a buffer of size 1
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null : "ERROR: (WINDOW) Cant get GLFWVideoMode";

            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }


        // Make the opengl context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        Logger.log("OpenGL version: " + glGetString(GL_VERSION));

        // Enable Vsync
        glfwSwapInterval(1);

        // Show window
        glfwShowWindow(window);

        imguilayer = new ImGuiLayer(Window.getWindow());
        imguilayer.init();
    }

    private void loop()
    {
        // Calculating delta time
        float beginTime;
        float endTime;
        float deltaTime = 1.0f;

        if(SceneManager.getCurrentScene() == null)
        {
            SceneManager.setScene(new CodeScene("assets/levels/cod1.csv"));
        }

        while(!glfwWindowShouldClose(window))
        {
            beginTime = Time.getTime();
            DebugDraw.beginFrame();
            SceneManager.getCurrentScene().renderer.prepare();


            SceneManager.getCurrentScene().update(deltaTime);
            DebugDraw.draw();
            imguilayer.update(deltaTime);

            if(Input.KeyboardListener.iskeyPressed(GLFW_KEY_F11))
            {
                setFullscreen();
            }

            Input.update();
            glfwSwapBuffers(window);
            glfwPollEvents();

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
        }

        close();
    }

    /**
     * Initialize the window
     * Set's up GLFW and GLFW callbacks
     * Starts the game loop
     */
    public void run()
    {
        init();
        loop();
    }

    private void close()
    {
        shoudlClose = true;
        imguilayer.dispose();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static long getWindow()
    {
        return get().window;
    }

    public void setFullscreen()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (!Config.isFullscreen)
        {
            glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0,0, mode.width(), mode.height(), mode.refreshRate());
            Config.isFullscreen = !Config.isFullscreen;
        }
        else
        {
            glfwSetWindowMonitor(window, NULL, (mode.width() - Config.w_width) / 2, (mode.height() - Config.w_height) / 2, Config.w_width, Config.w_height, mode.refreshRate());
            Config.isFullscreen = !Config.isFullscreen;
        }

    }
}
