package org.misterej.game.CODE.Events;

import org.misterej.engine.LevelEditor;
import org.misterej.engine.SceneManager;
import org.misterej.game.CODE.CodeScene;
import org.misterej.game.CODE.Enums.EventType;
import org.misterej.game.CODE.EventManager;

public class ResetEvent extends Event{
    public ResetEvent() {
        super(EventType.RESET);
        EventManager.clear();
        ((CodeScene)SceneManager.getCurrentScene()).loadLevel(SceneManager.getCurrentScene().getLevel());
    }
}
