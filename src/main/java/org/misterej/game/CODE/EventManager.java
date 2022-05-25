package org.misterej.game.CODE;

import org.misterej.game.CODE.Enums.Direction;
import org.misterej.game.CODE.Enums.EventType;
import org.misterej.game.CODE.Events.*;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private static List<Observer> observers = new ArrayList<>();

    public static void add(Observer observer)
    {
        if(!observers.contains(observer)) observers.add(observer);
    }

    public static void clear()
    {
        observers.clear();
    }

    public static void move()
    {
        notify(new Event(EventType.MOVE));
    }

    public static void pickup()
    {
        notify(new PickupEvent());
    }

    public static void drop()
    {
        notify(new DropEvent());
    }

    public static void use()
    {
        notify(new UseEvent());
    }

    public static void look(Direction dir)
    {
        notify(new LookEvent(dir));
    }

    public static void notify(Event event)
    {
        for(Observer observer : observers)
        {
            observer.onNotify(event);
        }
    }
}
