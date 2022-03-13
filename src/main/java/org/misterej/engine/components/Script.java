package org.misterej.engine.components;

import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.misterej.engine.GameObject;

public abstract class Script {
    protected GameObject gameObject;

    public Script(GameObject gameObject)
    {
        this.gameObject = gameObject;
    }

    public void beginCollision(GameObject obj, Contact contact, Vector2f normal){

    }

    public void endCollision(GameObject obj, Contact contact, Vector2f normal){

    }

    public void preSolve(GameObject obj, Contact contact, Vector2f normal){

    }

    public void postSolve(GameObject obj, Contact contact, Vector2f normal){

    }

    public abstract void update(float deltaTime);
    public abstract void start();
}
