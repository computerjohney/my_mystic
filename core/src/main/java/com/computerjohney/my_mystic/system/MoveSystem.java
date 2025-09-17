package com.computerjohney.my_mystic.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.computerjohney.my_mystic.component.Move;
import com.computerjohney.my_mystic.component.Transform;

public class MoveSystem extends IteratingSystem {

    public MoveSystem() {
        super(Family.all(Move.class, Transform.class).get());

    }


    protected void processEntity(Entity entity, float deltaTime) {

        Move move = Move.MAPPER.get(entity);
        if (move.isRooted() || move.getDirection().isZero()) {
            return;
        }

        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();
        position.set(
            position.x + move.getMaxSpeed() * move.getDirection().x * deltaTime,
            position.y + move.getMaxSpeed() * move.getDirection().y * deltaTime
        );
    }
}
