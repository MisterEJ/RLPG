package org.misterej.game;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.misterej.engine.Scene;

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

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
            //position              Color
             0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     // 0 // BOTTOM RIGHT
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     // 1 // TOP LEFT
             0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     // 2 // TOP RIGHT
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,      // 3 // BOTTOM LEFT
    };

    private int[] elementArray ={
            // COUNTER CLOCKWISE
            2, 1, 0,    // TOP LEFT TRIANGLE
            0, 1, 3,    // BOTTOM RIGHT TRIANGLE
    };



    @Override
    public void update(float deltaTime) {
        // Bind shader program
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);

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
        //Compile shaders
        // Create shader and pass the source (code) to the GPU

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        CompileShader(vertexID, vertexShaderSource);

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        CompileShader(fragmentID, fragmentShaderSource);

        // LINK the shaders and check for errors

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: FAILED TO LINK THE SHADERPROGRAM");
            System.out.println(glGetProgramInfoLog(len));
            assert false : "Shader linking failed";
        }

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
        int vertexSizeInBytes = (positionsSize + colorSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

    }

    private void CompileShader(int shaderID, String source)
    {
        //Compile the shader and check for errors
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: default.glsl Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "Shader Compilation failed";
        }
    }
}
