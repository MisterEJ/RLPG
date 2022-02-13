package org.misterej.engine;

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
        private boolean mouseButtonPressed[] = new boolean[3];
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
                }
            }
            else if(action == GLFW_RELEASE)
            {
                if(button < getInstance().mouseButtonPressed.length)
                {
                    getInstance().mouseButtonPressed[button] = false;
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

        }

        public static float getX()
        {
            return (float)getInstance().xPos;
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
    }

    public static class KeyboardListener
    {
        static KeyboardListener instance;
        private boolean keyPressed[] = new boolean[350];
        private boolean keyReleased[] = new boolean[350];
        private boolean keyDown[] = new boolean[350];

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
            /*
                Resets keyReleased and keyPressed arrays after every frame
             */
            for (int i = 0; i < getInstance().keyReleased.length; i++)
            {
                getInstance().keyReleased[i] = false;
            }
            for (int i = 0; i < getInstance().keyPressed.length; i++)
            {
                getInstance().keyPressed[i] = false;
            }
        }

        public static boolean iskeyPressed(int key)
        {
            if(key < getInstance().keyPressed.length)
            {
                return getInstance().keyPressed[key];
            }
            return false;
        }

        public static boolean isKeyReleased(int key)
        {
            if(key < getInstance().keyReleased.length)
            {
                return getInstance().keyReleased[key];
            }
            return false;
        }

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
