package org.misterej.game.CODE;

import org.misterej.game.CODE.Events.Event;

public interface Observer {
    void onNotify(Event event);
}
