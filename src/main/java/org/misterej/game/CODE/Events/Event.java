package org.misterej.game.CODE.Events;

import org.misterej.game.CODE.Enums.EventType;

public class Event {
    private final EventType type;

    public Event(EventType type)
    {
        this.type = type;
    }

    public EventType getType()
    {
        return this.type;
    }
}
