package org.misterej.game;

import org.misterej.engine.Scene;

import static org.lwjgl.opengl.GL11.*;

public class AnotherScene extends Scene {

    @Override
    public void update(float deltaTime) {
        glBegin(GL_QUADS);
        glVertex2d(-0.5, 0.5);
        glVertex2d(0.5, 0.5);
        glVertex2d(0.5, -0.5);
        glVertex2d(-0.5, -0.5);
        glEnd();
    }

    @Override
    public void init() {

    }
}
