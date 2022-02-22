package org.misterej.engine;

import org.joml.Vector2f;
import org.misterej.engine.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final String name;
    private List<Component> components;

    private Transform transfrom;

    public GameObject (String name)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transfrom = new Transform();
    }

    public GameObject (String name, Transform transform)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transfrom = transform;
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
        transfrom.position.add(distance);
        SpriteRenderer spr = getComponent(SpriteRenderer.class);
        if(spr != null)
        {
            spr.setDirtyFlag();
        }
    }

    public Transform getTransform()
    {
        return transfrom;
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


}
