package org.misterej.game.CODE.Events;

import org.misterej.game.CODE.Enums.Direction;
import org.misterej.game.CODE.Enums.EventType;

public class LookEvent extends Event{

    public final Direction direction;

    public LookEvent(Direction direction) {
        super(EventType.LOOK);
        this.direction = direction;
    }
}
