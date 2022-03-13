package org.misterej.engine.dyn4j.components;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.misterej.engine.Component;
import org.misterej.engine.physics2d.enums.BodyType;

public class RigidBody2D extends Component {

    private MassType mass = MassType.NORMAL;

    private Body rawBody;

    @Override
    public void update(float deltaTime) {
        if(rawBody != null)
        {
            gameObject.transform.position.x = (float)rawBody.getTransform().getTranslationX();
            gameObject.transform.position.y = (float)rawBody.getTransform().getTranslationY();
            gameObject.transform.rotation = (float)rawBody.getTransform().getRotation().toDegrees();
            gameObject.setDirtyFlag();
        }
    }

    @Override
    public void start() {

    }

    public MassType getMass() {
        return mass;
    }

    public void setMass(MassType mass) {
        this.mass = mass;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    public Body getRawBody() {
        return rawBody;
    }
}
