package org.misterej.engine;

import org.joml.*;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private Vector2f viewPort;

    private final float ASPECT = 16f / 9f;

    public Camera(Vector2f position, Vector2f viewPort)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.viewPort = new Vector2f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public Camera(Vector2f position)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.viewPort = new Vector2f(Config.view_width, Config.view_height);
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }


    public Vector2f getViewPort() {
        return viewPort;
    }

    /**
     * Adjust the projection matrix automatically called when setting a view port
     */
    public void adjustProjection()
    {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, viewPort.x, 0.0f, viewPort.y, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    /**
     * Set the cameras viewport and adjust the projection
     * @param viewPort Vector2f
     */
    public void setViewPort(Vector2f viewPort)
    {
        this.viewPort = viewPort;
        adjustProjection();
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 0.0f),
                                            cameraFront.add(position.x,position.y,0.0f),
                                            cameraUp);

        viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }
}
