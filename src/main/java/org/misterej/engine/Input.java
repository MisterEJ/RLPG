package org.misterej.engine;

import org.joml.Vector4f;

import java.util.Arrays;
import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    public static void update()
    {
        MouseListener.endFrame();
        KeyboardListener.endFrame();
    }

    public static class MouseListener
    {
        private static MouseListener instance;
        private double scrollX, scrollY;
        private double xPos, yPos, lastX, lastY;
        private boolean[] mouseButtonPressed = new boolean[3];
        private boolean[] mouseButtonReleased = new boolean[3];
        private boolean[] mouseButtonDown = new boolean[3];
        private boolean isDragging;

        private MouseListener()
        {
            this.scrollX = 0.0;
            this.scrollY = 0.0;
            this.xPos = 0.0;
            this.yPos = 0.0;
            this.lastX = 0.0;
            this.lastY = 0.0;
            this.isDragging = false;
        }

        public static MouseListener getInstance() {
            if(instance == null) {
                instance = new MouseListener();
            }

            return instance;
        }

        public static void mousePosCallback(long window, double xpos, double ypos)
        {
            getInstance().lastX = getInstance().xPos;
            getInstance().lastY = getInstance().yPos;
            getInstance().xPos = xpos;
            getInstance().yPos = ypos;
            getInstance().isDragging = getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[0];
        }

        public static void mouseButtonCallback(long window, int button, int action, int mods)
        {
            if(action == GLFW_PRESS)
            {
                if(button < getInstance().mouseButtonPressed.length)
                {
                    getInstance().mouseButtonPressed[button] = true;
                    getInstance().mouseButtonDown[button] = true;
                }
            }
            else if(action == GLFW_RELEASE)
            {
                if(button < getInstance().mouseButtonPressed.length)
                {
                    getInstance().mouseButtonReleased[button] = true;
                    getInstance().mouseButtonDown[button] = false;
                    getInstance().isDragging = false;
                }
            }
        }

        public static void mouseScrollCallback(long window, double xOffset, double yOffset)
        {
            getInstance().scrollX = xOffset;
            getInstance().scrollY = yOffset;
        }

        public static void endFrame()
        {
            getInstance().scrollY = 0.0;
            getInstance().scrollX = 0.0;
            getInstance().lastX = getInstance().xPos;
            getInstance().lastY = getInstance().yPos;

            Arrays.fill(getInstance().mouseButtonReleased, false);
            Arrays.fill(getInstance().mouseButtonPressed, false);

        }

        public static float getX()
        {
            return (float)getInstance().xPos;
        }

        public static float getXWorld()
        {
            float currentX = getX();
            currentX = (currentX / Config.w_width) * 2 - 1;
            Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
            tmp.mul(SceneManager.getCurrentScene().getCamera().getInverseProjection()).mul(SceneManager.getCurrentScene().getCamera().getInverseView());
            currentX = tmp.x;
            return currentX;
        }

        public static float getYWorld()
        {
            float currentY = Config.w_height - getY();
            currentY = (currentY / Config.w_height) * 2 - 1;
            Vector4f tmp = new Vector4f(0, currentY, 0, 1);
            tmp.mul(SceneManager.getCurrentScene().getCamera().getInverseProjection()).mul(SceneManager.getCurrentScene().getCamera().getInverseView());
            currentY = tmp.y;
            return currentY;
        }

        public static float getY()
        {
            return (float)getInstance().yPos;
        }

        public static float getDx()
        {
            return (float)(getInstance().lastX - getInstance().xPos);
        }

        public static float getDy()
        {
            return (float)(getInstance().lastY - getInstance().yPos);
        }

        public static float getScrollX()
        {
            return (float)getInstance().scrollX;
        }

        public static float getScrollY()
        {
            return (float)getInstance().scrollY;
        }

        public static boolean isDragging()
        {
            return getInstance().isDragging;
        }

        public static boolean isMouseButtonPressed(int button)
        {
            if(button < getInstance().mouseButtonPressed.length)
                return getInstance().mouseButtonPressed[button];
            else
                return false;
        }

        public static boolean isMouseButtonDown(int button)
        {
            if(button < getInstance().mouseButtonDown.length)
                return getInstance().mouseButtonDown[button];
            else
                return false;
        }

        public static boolean isMouseButtonReleased(int button)
        {
            if(button < getInstance().mouseButtonReleased.length)
                return getInstance().mouseButtonReleased[button];
            else
                return false;
        }
    }

    public static class KeyboardListener
    {
        static KeyboardListener instance;
        private boolean[] keyPressed = new boolean[350];
        private boolean[] keyReleased = new boolean[350];
        private boolean[] keyDown = new boolean[350];

        public static KeyboardListener getInstance() {
            if(instance == null)
                instance = new KeyboardListener();
            return instance;
        }

        public static void keyCallback(long window, int key, int scancode, int action, int mods)
        {
            if(action == GLFW_PRESS)
            {
                if(key < getInstance().keyPressed.length)
                {
                    getInstance().keyPressed[key] = true;
                    getInstance().keyDown[key] = true;
                }
            }
            else if(action == GLFW_RELEASE)
            {
                if(key < getInstance().keyPressed.length)
                {
                    getInstance().keyDown[key] = false;
                    getInstance().keyReleased[key] = true;
                }
            }
        }

        private static void resetKeys()
        {
            Arrays.fill(getInstance().keyReleased, false);
            Arrays.fill(getInstance().keyPressed, false);
        }


        /**
         * Check to see if a specific key was pressed
         * @param key int
         * @return boolean
         */
        public static boolean iskeyPressed(int key)
        {
            if(key < getInstance().keyPressed.length)
            {
                return getInstance().keyPressed[key];
            }
            return false;
        }


        /**
         * Check to see if a specific key was released
         * @param key int
         * @return boolean
         */
        public static boolean isKeyReleased(int key)
        {
            if(key < getInstance().keyReleased.length)
            {
                return getInstance().keyReleased[key];
            }
            return false;
        }


        /**
         * Check to see if a specific key is currently being pressed
         * @param key int
         * @return boolean
         */
        public static boolean isKeyDown(int key)
        {
            if(key < getInstance().keyDown.length)
            {
                return getInstance().keyDown[key];
            }
            return false;
        }

        public static void endFrame()
        {
            resetKeys();
        }
    }

}
