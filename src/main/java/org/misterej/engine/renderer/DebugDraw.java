package org.misterej.engine.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.misterej.engine.SceneManager;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.JMath;
import org.misterej.engine.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class DebugDraw {
    private static final int MAX_LINES = 1000;
    private static final int VERTEX_SIZE = 3;
    private static final int VERTICES_PER_LINE = 2;
    private static List<Line2D> lines = new ArrayList<>();

    private static float[] vertexArray = new float[MAX_LINES * VERTEX_SIZE * VERTICES_PER_LINE];
    private static Shader shader;

    private static int vaoID, vboID;

    private static boolean started = false;

    public static void start()
    {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, VERTEX_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        shader = AssetPool.getShader("assets/shaders/debugdraw.glsl");
    }

    public static void begin_frame()
    {
        if(!started)
        {
            start();
            started = true;
        }
    }

    public static int getVertexArraySize()
    {
        return lines.size() * VERTEX_SIZE * VERTICES_PER_LINE;
    }

    public static void draw()
    {
        int index = 0;
        for(Line2D line : lines)
        {
            for (int i = 0; i < 2; i++)
            {
                Vector2f pos = i == 0 ? line.getFrom() : line.getTo();
                vertexArray[index] = pos.x;
                vertexArray[index + 1] = pos.y;
                vertexArray[index + 2] = -1f;
                index += 3;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, getVertexArraySize()));

        shader.use();
        shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_LINES, 0, lines.size() * 2);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.detach();
    }

    public static void addLine2D(Vector2f from, Vector2f to)
    {
        DebugDraw.lines.add(new Line2D(from, to));
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                JMath.rotate(vert, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1]);
        addLine2D(vertices[0], vertices[3]);
        addLine2D(vertices[1], vertices[2]);
        addLine2D(vertices[2], vertices[3]);
    }
}
