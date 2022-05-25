package org.misterej.engine.components;

import org.misterej.engine.Component;

public class TestComponent extends Component {

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void start()
    {
        System.out.println("TestCOmponent");
    }
}
