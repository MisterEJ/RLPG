package org.misterej.engine.components;

import org.misterej.engine.Component;

public class ScriptComponent extends Component {

    private Script script;

    public ScriptComponent(Script script)
    {
        this.script = script;
    }

    @Override
    public void update(float deltaTime) {
        script.update(deltaTime);
    }

    @Override
    public void start() {
        script.start();
    }
}
