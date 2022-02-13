package org.misterej.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window {
    private long window;
    public final String title;

    public Window(String title)
    {
        this.title = title;
        init();
    }

    private void init()
    {
        // ERROR CALLBACK SETUP
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // CONFIGURE GLFW and CREATE THE WINDOW
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(Config.w_width, Config.w_height, title, 0, 0);
        if(window == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        // SET CALLBACKS

        glfwSetCursorPosCallback(window, Input.MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, Input.MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, Input.MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(window, Input.KeyboardListener::keyCallback);

        // Center the window
        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window); // OpenGL context
        glfwSwapInterval(1); // VSync
        glfwShowWindow(window); // Show window
    }

    public boolean shouldClose()
    {
        return glfwWindowShouldClose(window);
    }

    public void update()
    {
        Input.update();
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void close()
    {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void setFullscreen()
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
