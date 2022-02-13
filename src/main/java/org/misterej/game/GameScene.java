package org.misterej.game;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.misterej.engine.Camera;
import org.misterej.engine.Scene;
import org.misterej.engine.renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class GameScene extends Scene {

    private final String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private final String vertexShaderSource = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private Shader defaultShader;
    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
            //position                  Color                           UV
             100.0f, 0.0f,   0.0f,      1.0f, 0.0f, 0.0f, 1.0f,         1, 0,    // 0 // BOTTOM RIGHT
             0.0f,   100.0f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,         0, 1,    // 1 // TOP LEFT
             100.0f, 100.0f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,         1, 1,    // 2 // TOP RIGHT
             0.0f,   0.0f,   0.0f,      1.0f, 1.0f, 0.0f, 1.0f,         0, 0,    // 3 // BOTTOM LEFT
    };

    private int[] elementArray ={
            // COUNTER CLOCKWISE
            2, 1, 0,    // TOP LEFT TRIANGLE
            0, 1, 3,    // BOTTOM RIGHT TRIANGLE
    };



    @Override
    public void update(float deltaTime) {
        camera.position.x += deltaTime * -5.0f;
        camera.position.y += deltaTime * -5.0f;
        // Bind shader program
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
        defaultShader.detach();


//        glBegin(GL_TRIANGLES);
//        glVertex3f(0.0f, -0.5f, 0.0f);
//        glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
//        glVertex3f(-0.5f, 0.5f, 0.0f);
//        glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
//        glVertex3f(0.5f, 0.5f, 0.0f);
//        glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
//        glEnd();

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // GENERATE VBO, VAO, EBO buffer objects and send them to the GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of verticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create Int buffer of elements
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeInBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeInBytes, (positionsSize + colorSize * Float.BYTES));
        glEnableVertexAttribArray(2);
    }


}
