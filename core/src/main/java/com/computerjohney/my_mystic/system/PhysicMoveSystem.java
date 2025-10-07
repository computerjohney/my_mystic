package com.computerjohney.my_mystic.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.computerjohney.my_mystic.component.Move;
import com.computerjohney.my_mystic.component.Physic;
import com.computerjohney.my_mystic.component.Transform;

public class PhysicMoveSystem extends IteratingSystem {

    private final Vector2 normalizedDirection = new Vector2();

    public PhysicMoveSystem() {
        super(Family.all(Physic.class, Move.class).get());
    }

    protected void processEntity(Entity entity, float deltaTime) {

        Move move = Move.MAPPER.get(entity);
        Body body = Physic.MAPPER.get(entity).getBody();
        if (move.isRooted() || move.getDirection().isZero()) {
            body.setLinearVelocity(0f,0f);
            return;
        }

        // normalized for diagonal movement
        normalizedDirection.set(move.getDirection()).nor();

        Physic.MAPPER.get(entity).getBody().setLinearVelocity(

          move.getMaxSpeed() * normalizedDirection.x,
            move.getMaxSpeed() * normalizedDirection.y
        );
    }
}
