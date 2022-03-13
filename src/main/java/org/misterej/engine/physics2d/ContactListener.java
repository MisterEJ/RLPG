package org.misterej.engine.physics2d;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.components.ScriptComponent;
import org.misterej.engine.physics2d.components.RigidBody2D;

public class ContactListener implements org.jbox2d.callbacks.ContactListener {
    @Override
    public void beginContact(Contact contact) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        if(objA.getComponent(ScriptComponent.class) != null)
            objA.getComponent(ScriptComponent.class).getScript().beginCollision(objB, contact, aNormal);
        if(objB.getComponent(ScriptComponent.class) != null)
            objB.getComponent(ScriptComponent.class).getScript().beginCollision(objA, contact, bNormal);
    }

    @Override
    public void endContact(Contact contact) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        if(objA.getComponent(ScriptComponent.class) != null)
            objA.getComponent(ScriptComponent.class).getScript().endCollision(objB, contact, aNormal);
        if(objB.getComponent(ScriptComponent.class) != null)
            objB.getComponent(ScriptComponent.class).getScript().endCollision(objA, contact, bNormal);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        if(objA.getComponent(ScriptComponent.class) != null)
            objA.getComponent(ScriptComponent.class).getScript().preSolve(objB, contact, aNormal);
        if(objB.getComponent(ScriptComponent.class) != null)
            objB.getComponent(ScriptComponent.class).getScript().preSolve(objA, contact, bNormal);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        if(objA.getComponent(ScriptComponent.class) != null)
            objA.getComponent(ScriptComponent.class).getScript().postSolve(objB, contact, aNormal);
        if(objB.getComponent(ScriptComponent.class) != null)
            objB.getComponent(ScriptComponent.class).getScript().postSolve(objA, contact, bNormal);
    }
}
