package org.misterej.engine;

import org.joml.Vector2f;
import org.misterej.engine.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameObject{

    private String name;
    private List<Component> components;

    public Transform transform;
    private final int id;
    private static int nextID = 0;

    public GameObject (String name)
    {
        this.id = nextID;
        nextID++;
        this.name = name;
        components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject (String name, Transform transform)
    {
        this.id = nextID;
        nextID++;
        this.name = name;
        components = new ArrayList<>();
        this.transform = transform;
    }

    /**
     * Get a specific component
     * @param <T> componentClass
     */
    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component c : components)
        {
            if(componentClass.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return componentClass.cast(c);
                }
                catch(ClassCastException e)
                {
                    assert false: "ERROR: Error while casting component";
                }
            }
        }

        return null;
    }


    /**
     * Remove a specific component from the object.
     * @param <T> componentClass
     */
    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(componentClass.isAssignableFrom(components.get(i).getClass()))
            {
                components.remove(i);
                return;
            }
        }
    }


    /**
     * Add a specific component from the object.
     */
    public void addComponent(Component c)
    {
        this.components.add(c);
        c.gameObject = this;
    }

    public void move(Vector2f distance)
    {
        transform.position.add(distance);
        SpriteRenderer spr = getComponent(SpriteRenderer.class);
        if(spr != null)
        {
            spr.setDirtyFlag();
        }
    }

    public void setPos(Vector2f pos)
    {
        transform.position = pos;
        SpriteRenderer spr = getComponent(SpriteRenderer.class);
        if(spr != null)
        {
            spr.setDirtyFlag();
        }
    }

    public void imgui()
    {
        for(Component c : components)
        {
            c.imgui();
        }
    }

    public Transform getTransform()
    {
        return transform;
    }

    /**
     * Calls the update method for the components
     */
    public void update(float deltaTime)
    {
        for(Component c : this.components)
        {
            c.update(deltaTime);
        }
    }

    /**
     * Calls the start method for the components
     */
    public void start()
    {
        for(Component c : this.components)
        {
            c.start();
        }
    }

    public float getX()
    {
        return transform.position.x;
    }

    public float getY()
    {
        return transform.position.y;
    }

    public Vector2f getPosition()
    {
        return transform.position;
    }

    public int getId() {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDirtyFlag()
    {
        SpriteRenderer spr = getComponent(SpriteRenderer.class);
        if(spr != null)
        {
            spr.setDirtyFlag();
        }
    }

    public void rotate(float degrees)
    {
        transform.rotation += degrees;

        if(transform.rotation > 360f)
            transform.rotation -= 360f;

        if(transform.rotation < 0)
            transform.rotation += 360f;

        setDirtyFlag();
    }
}
