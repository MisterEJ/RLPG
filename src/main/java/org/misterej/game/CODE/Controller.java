package org.misterej.game.CODE;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.misterej.engine.GameObject;
import org.misterej.engine.Scene;
import org.misterej.engine.SceneManager;
import org.misterej.engine.components.*;
import org.misterej.engine.physics2d.components.RigidBody2D;
import org.misterej.engine.util.AssetPool;
import org.misterej.engine.util.JMath;
import org.misterej.engine.util.Timer;
import org.misterej.game.CODE.Enums.Direction;
import org.misterej.game.CODE.Enums.EventType;
import org.misterej.game.CODE.Enums.Item;
import org.misterej.game.CODE.Events.Event;
import org.misterej.game.CODE.Events.LookEvent;
import org.python.util.PythonInterpreter;

import java.util.*;

public class Controller extends Script implements Observer{

    private PythonInterpreter interpreter = new PythonInterpreter();
    private Queue<Event> steps = new LinkedList<>();

    private List<Integer> wall = Arrays.asList(9,10,11,12,13,15,17,18,19,23);

    private Scene scene;
    private AnimationPlayer animationPlayer;
    private RigidBody2D rb;
    private Item item = Item.NONE;
    private Direction direction = Direction.DOWN;
    private final float cell_size = 0.5f;
    private Timer eventTimer = new Timer();
    private Timer endLevel = new Timer();
    private Vector2f statringPos = new Vector2f();
    private Vector2f walkToPos = new Vector2f();


    public Controller(GameObject gameObject) {
        super(gameObject);
        EventManager.add(this);
        gameObject.addComponent(new AnimationPlayer());
    }

    @Override
    public void update(float deltaTime) {

        if(animationPlayer.getAnimationName().startsWith("W_"))
        {
            switch (direction)
            {
                case DOWN -> rb.getRawBody().setTransform(new Vec2(gameObject.getPosition().x, gameObject.getPosition().y -= deltaTime), 0);
                case RIGHT -> rb.getRawBody().setTransform(new Vec2(gameObject.getPosition().x += deltaTime, gameObject.getPosition().y), 0);
                case LEFT ->  rb.getRawBody().setTransform(new Vec2(gameObject.getPosition().x -= deltaTime, gameObject.getPosition().y), 0);
                case UP -> rb.getRawBody().setTransform(new Vec2(gameObject.getPosition().x, gameObject.getPosition().y += deltaTime), 0);
            }
            if(direction == Direction.LEFT && gameObject.getPosition().x <= walkToPos.x) stopmoving();
            if(direction == Direction.RIGHT && gameObject.getPosition().x >= walkToPos.x) stopmoving();
            if(direction == Direction.UP && gameObject.getPosition().y >= walkToPos.y) stopmoving();
            if(direction == Direction.DOWN && gameObject.getPosition().y <= walkToPos.y) stopmoving();
        }

        if(animationPlayer.getAnimationName().equals("IDLE"))
            eventTimer.update(deltaTime);
        if(eventTimer.isTime())
        {
            processEvent();
            eventTimer.reset();
        }

        if(!animationPlayer.getAnimationName().equals("IDLE") && !animationPlayer.isPlaying() && !animationPlayer.getAnimationName().equals("DEATH") && !animationPlayer.getAnimationName().equals("COMPLETE"))
        {
            animationPlayer.setAnimation("IDLE", false);
            look(direction);
        }

        if(animationPlayer.getAnimationName().equals("COMPLETE"))
        {
            endLevel.update(deltaTime);
            if(endLevel.isTime()) ((CodeScene)SceneManager.getCurrentScene()).change_level();
        }
    }

    public void stopmoving()
    {
        rb.getRawBody().setTransform(new Vec2(walkToPos.x, walkToPos.y), 0);
        animationPlayer.setAnimation("IDLE", false);
        look(direction);
    }

    @Override
    public void start() {
        rb = gameObject.getComponent(RigidBody2D.class);
        animationPlayer = gameObject.getComponent(AnimationPlayer.class);
        statringPos.x = gameObject.getPosition().x;
        statringPos.y = gameObject.getPosition().y;

        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/codesprites.png");
        SpriteSheet doggoSheet = AssetPool.getSpriteSheet("assets/textures/doggoanimated.png");
        animationPlayer.addAnimation(new Animation("IDLE", spriteSheet, false, new int[]{0,1,2,3}));
        animationPlayer.addAnimation(new Animation("ARIGHT", doggoSheet, false, new int[]{0,1}));
        animationPlayer.addAnimation(new Animation("ALEFT", doggoSheet, false, new int[]{2,3}));
        animationPlayer.addAnimation(new Animation("AUP", doggoSheet, false, new int[]{4,5}));
        animationPlayer.addAnimation(new Animation("ADOWN", doggoSheet, false, new int[]{6,7}));
        animationPlayer.addAnimation(new Animation("DEATH", doggoSheet, false, new int[]{10,10,31,10,10,31,10,10,8,9}));
        animationPlayer.addAnimation(new Animation("W_DOWN", doggoSheet, true, new int[]{11,12,13,14}));
        animationPlayer.addAnimation(new Animation("W_RIGHT", doggoSheet, true, new int[]{15,16,17,18}));
        animationPlayer.addAnimation(new Animation("W_LEFT", doggoSheet, true, new int[]{19,20,21,22}));
        animationPlayer.addAnimation(new Animation("W_UP", doggoSheet, true, new int[]{23,24,25,26}));
        animationPlayer.addAnimation(new Animation("COMPLETE", doggoSheet, true, new int[]{27,28,29,28}));
        animationPlayer.setAnimation("IDLE", false);
        animationPlayer.setFrame(2);

        eventTimer.setInterval(0.1f);
        endLevel.setInterval(2.5f);
    }

    @Override
    public void onNotify(Event event) {
        if(event.getType() != EventType.MESSAGE)
            steps.add(event);
    }

    public void processEvent()
    {
        Event event = steps.poll();
        if(event == null) return;
        if(event.getType() == EventType.LOOK)
        {
            look(((LookEvent)event).direction);
        }

        if(event.getType() == EventType.MOVE)
        {
            System.out.println(gameObject.getPosition());
            move();
        }

        if(event.getType() == EventType.PICKUP)
        {
            pickUp();
        }

        if(event.getType() == EventType.DROP)
        {
            drop();
        }

        if(event.getType() == EventType.USE)
        {
            use();
        }
    }

    private void look(Direction dir)
    {
        this.direction = dir;
        switch (direction)
        {
            case DOWN -> {
                animationPlayer.setFrame(2);
            }
            case UP -> {
                animationPlayer.setFrame(3);
            }
            case LEFT -> {
                animationPlayer.setFrame(1);
            }
            case RIGHT -> {
                animationPlayer.setFrame(0);
            }
        }
    }

    private void move()
    {
        switch (direction)
        {
            case DOWN -> {
                move(new Vector2f(gameObject.getX(), gameObject.getY() - cell_size));
            }
            case UP -> {
                move(new Vector2f(gameObject.getX(), gameObject.getY() + cell_size));
            }
            case LEFT -> {
                move(new Vector2f(gameObject.getX() - cell_size, gameObject.getY()));
            }
            case RIGHT -> {
                move(new Vector2f(gameObject.getX() + cell_size, gameObject.getY()));
            }
        }
    }

    private void move(Vector2f position)
    {
        GameObject go = SceneManager.getCurrentScene().getGameObjectByPos(position, new GameObject[]{gameObject});
        if(go != null && go.getComponent(SpriteRenderer.class).getSprite().getId() == 21)
        {
            animationPlayer.setAnimation("DEATH", true);
            return;
        }
        if(go == null || !wall.contains(go.getComponent(SpriteRenderer.class).getSprite().getId()))
        {
            walkTo(position);
        }
    }

    private void walkTo(Vector2f position)
    {
        walkToPos = position;
        switch (direction)
        {
            case DOWN -> {
                animationPlayer.setAnimation("W_DOWN", true);
            }
            case UP -> {
                animationPlayer.setAnimation("W_UP", true);
            }
            case LEFT -> {
                animationPlayer.setAnimation("W_LEFT", true);
            }
            case RIGHT -> {
                animationPlayer.setAnimation("W_RIGHT", true);
            }
        }
    }

    private void pickUp()
    {

        GameObject go = SceneManager.getCurrentScene().getGameObjectByPos(gameObject.getPosition(), new GameObject[]{gameObject});
        if(go != null)
        {
            for(Item i : Item.values())
            {
                if(Prefabs.items.getOrDefault(i, -1) == go.getComponent(SpriteRenderer.class).getSprite().getId())
                {
                    if(item != Item.NONE)
                        drop();
                    else animate_interact();

                    this.item = i;
                    SceneManager.getCurrentScene().removeGameObject(go);
                    return;
                }
            }
        }
    }

    private void drop()
    {
        if(item != Item.NONE && item != null)
        {
            animate_interact();
            Prefabs.create_item(item, gameObject.getPosition(), AssetPool.getSpriteSheet("assets/textures/codesprites.png"));
        }
        item = Item.NONE;
    }

    private void animate_interact()
    {
        switch (direction)
        {
            case DOWN -> {
                animationPlayer.setAnimation("ADOWN", true);
            }
            case UP -> {
                animationPlayer.setAnimation("AUP", true);
            }
            case LEFT -> {
                animationPlayer.setAnimation("ALEFT", true);
            }
            case RIGHT -> {
                animationPlayer.setAnimation("ARIGHT", true);
            }
        }
    }


    private GameObject looking_at()
    {
        switch (direction)
        {
            case RIGHT -> {
                return SceneManager.getCurrentScene().getGameObjectByPos(new Vector2f(gameObject.getPosition()).add(0.5f, 0), new GameObject[]{gameObject});
            }
            case LEFT -> {
                return SceneManager.getCurrentScene().getGameObjectByPos(new Vector2f(gameObject.getPosition()).add(-0.5f, 0), new GameObject[]{gameObject});
            }
            case UP -> {
                return SceneManager.getCurrentScene().getGameObjectByPos(new Vector2f(gameObject.getPosition()).add(0 , 0.5f), new GameObject[]{gameObject});
            }
            case DOWN -> {
                return SceneManager.getCurrentScene().getGameObjectByPos(new Vector2f(gameObject.getPosition()).add(0 , -0.5f), new GameObject[]{gameObject});
            }
            default -> {
                return null;
            }
        }
    }

    private void use()
    {
        GameObject go = looking_at();
        if(go != null && go.getName().equals("Princess") && item == Item.CROWN)
        {
            go.getComponent(SpriteRenderer.class).setSprite(AssetPool.getSpriteSheet("assets/textures/codesprites.png").getSprite(16));
            item = Item.NONE;
            animationPlayer.setAnimation("COMPLETE", true);
        } else if(go != null && go.getComponent(SpriteRenderer.class).getSprite().getId() == 23 && item == Item.KEY)
        {
            go.getComponent(SpriteRenderer.class).setSprite(AssetPool.getSpriteSheet("assets/textures/codesprites.png").getSprite(22));
            item = item.NONE;
            animate_interact();
        } else if(go != null && go.getComponent(SpriteRenderer.class).getSprite().getId() == 21 && item == Item.SWORD)
        {
            SceneManager.getCurrentScene().removeGameObject(go);
            animate_interact();
        }
    }



    public void exec(String script)
    {
        script = "from org.misterej.game.CODE import EventManager as player\n" +
                "from org.misterej.game.CODE.Enums import Direction as DIR\n" + script;
        try
        {
            interpreter.exec(script);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
