package org.misterej.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private Component p;
    private final String name;
    private List<Component> components;

    public GameObject (String name)
    {
        this.name = name;
        this.components = new ArrayList<>();
    }

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

    public void addComponent(Component c)
    {
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float deltaTime)
    {
        for(Component c : this.components)
        {
            c.update(deltaTime);
        }
    }

    public void start()
    {
        for(Component c : this.components)
        {
            c.start();
        }
    }


}
