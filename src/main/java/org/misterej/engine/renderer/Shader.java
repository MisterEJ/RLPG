package org.misterej.engine.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.misterej.engine.util.Logger;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean isBeingUsed = false;

    private String filePath;
    private String fragmentSource;
    private String vertexSource;


    public Shader(String filePath)
    {
        this.filePath = filePath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] sourceSplit = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex"))
            {
                vertexSource = sourceSplit[1];
            } else if(firstPattern.equals("fragment"))
            {
                fragmentSource = sourceSplit[1];
            } else throw new IOException("Unexpected token '" + firstPattern + "'");

            if(secondPattern.equals("vertex"))
            {
                vertexSource = sourceSplit[2];
            } else if(secondPattern.equals("fragment"))
            {
                fragmentSource = sourceSplit[2];
            } else throw new IOException("Unexpected token '" + secondPattern + "'");

        } catch (IOException e)
        {
            e.printStackTrace();
            assert false : "ERROR:(Shader) Could not open file: " + filePath;
        }
    }

    public void compile()
    {
        Logger.log("Compiling shader: " + filePath);
        // Compiles and links the shaderProgram
        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        CompileShader(vertexID, vertexSource);

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        CompileShader(fragmentID, fragmentSource);

        // LINK the shaders and check for errors

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: FAILED TO LINK THE SHADERPROGRAM");
            System.out.println(glGetProgramInfoLog(len));
            assert false : "ERROR:(Shader) Shader linking failed";
        }

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
            Logger.logGL(glGetError());
            System.out.println("ERROR:(Shader) "+ filePath +" Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "ERROR:(Shader) Shader Compilation failed";
        }
    }

    public void use()
    {
        if(!isBeingUsed)
        {
            glUseProgram(shaderProgramID);
            isBeingUsed = true;
        }
    }

    public void detach()
    {
        if(isBeingUsed)
        {
            glUseProgram(0);
            isBeingUsed = false;
        }
    }

    public void uploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadMat2f(String varName, Matrix2f mat2)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        mat2.get(matBuffer);
        glUniformMatrix2fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float f)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, f);
    }

    public void uploadInt(String varName, int i)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, i);
    }

    public void uploadTexture(String varName, int slot)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }
}
