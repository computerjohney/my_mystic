package com.computerjohney.my_mystic.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.computerjohney.my_mystic.component.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.computerjohney.my_mystic.component.Physic;

public class PhysicSystem extends IteratingSystem implements EntityListener {

    private final World world;
    private final float interval;
    private float accumulator;          // sum the deltas

    public PhysicSystem(World world, float interval) {
        super(Family.all(Physic.class, Transform.class).get());
        this.world = world;
        this.interval = interval;
        this.accumulator = 0f;
    }


    // override Ashley add get family, add this system as an entity listener
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(getFamily(), this);
    }

    // eg. leaves family...
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }


    // override ashley's process entity by...
    public void update(float deltaTime) {
        this.accumulator += deltaTime;

        // while smaller than acc we don't do any physic simulation (ie call update)
        while (this.accumulator >= this.interval) {
            this.accumulator -= this.interval;
            super.update(interval);
            this.world.step(interval, 6, 2);
        }
        world.clearForces();

        // alpha 0...1 rendering smoothed, sets position
        float alpha = this.accumulator / this.interval;
        for (int i = 0; i < getEntities().size(); ++i) {
            this.interpolateEntity(getEntities().get(i), alpha);
        }
    }

    protected void processEntity(Entity entity, float deltaTime) {
        Physic physic = Physic.MAPPER.get(entity);
        physic.getPrevPosition().set(physic.getBody().getPosition());   // pos b4 phy sim
    }

    /**
     * Interpolates entity position between physics steps.
     */
    private void interpolateEntity(Entity entity, float alpha) {
        Transform transform = Transform.MAPPER.get(entity);
        Physic physic = Physic.MAPPER.get(entity);

        // linear interpolation
        transform.getPosition().set(
            MathUtils.lerp(physic.getPrevPosition().x, physic.getBody().getPosition().x, alpha),
            MathUtils.lerp(physic.getPrevPosition().y, physic.getBody().getPosition().y, alpha)
        );
    }

    @Override
    public void entityAdded(Entity entity) {
    }

    @Override
    public void entityRemoved(Entity entity) {
        // !!! Important !!!
        // This does not work if the Physic component gets removed from an entity
        // because the component is no longer accessible here.
        // This ONLY works when an entity with a Physic component gets removed entirely from the engine.
        Physic physic = Physic.MAPPER.get(entity);
        if (physic != null) {
            Body body = physic.getBody();
            body.getWorld().destroyBody(body);
        }
    }
}
