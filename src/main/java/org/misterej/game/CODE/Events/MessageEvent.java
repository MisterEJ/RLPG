package org.misterej.game.CODE.Events;

import org.misterej.game.CODE.Enums.EventType;

public class MessageEvent extends Event{

    public final String message;

    public MessageEvent(String message) {
        super(EventType.MESSAGE);
        this.message = message;
    }
}
