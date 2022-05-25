package org.misterej.game.CODE;

import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.SceneManager;
import org.misterej.engine.Transform;
import org.misterej.engine.components.SpriteRenderer;
import org.misterej.engine.components.SpriteSheet;
import org.misterej.game.CODE.Enums.Item;

import java.util.HashMap;
import java.util.Map;

public class Prefabs {

    public final static Map<Item, Integer> items = new HashMap<>() {
        {
            put(Item.BOMB, 4);
            put(Item.COIN, 5);
            put(Item.SWORD, 6);
            put(Item.CROWN, 7);
            put(Item.KEY, 8);
        }
    };

    public static void create_item(Item item, Vector2f position, SpriteSheet spriteSheet)
    {
        GameObject gameObject = new GameObject("Item: " + item, new Transform(position, new Vector2f(0.5f, 0.5f)));
        gameObject.addComponent(new SpriteRenderer(spriteSheet.getSprite(items.get(item))));
        gameObject.getComponent(SpriteRenderer.class).setZIndex(1);
        SceneManager.getCurrentScene().addGameObject(gameObject);
        System.out.println();
    }
}
