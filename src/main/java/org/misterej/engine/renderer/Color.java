package org.misterej.engine.renderer;

// TODO: Color class

public class Color {

    private float r;
    private float g;
    private float b;
    private float a;

    public static Color Red = new Color(1.0f, 0.0f, 0.0f, 0.0f);
    public static Color Green = new Color(0.0f, 1.0f, 0.0f, 0.0f);
    public static Color Blue = new Color(0.0f, 0.0f, 1.0f, 0.0f);
    public static Color Black = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    public static Color White = new Color(1.0f, 1.0f, 1.0f, 0.0f);

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(Color color)
    {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public Color()
    {
        this.r = 0.0f;
        this.g = 0.0f;
        this.b = 0.0f;
        this.a = 0.0f;
    }

    public Color(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 0.0f;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

}
