package com.computerjohney.my_mystic.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class PhysicDebugRenderSystem extends EntitySystem implements Disposable {

    private final World physicWorld;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final ShapeRenderer shapeRenderer;
    private final Camera camera;

    public PhysicDebugRenderSystem(World physicWorld, Camera camera) {
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.shapeRenderer = new ShapeRenderer();
        this.physicWorld = physicWorld;
        this.camera = camera;

        setProcessing(true); // true is Default!
        //setProcessing(false);
    }

    @Override
    public void update(float deltaTime) {
        this.box2DDebugRenderer.render(physicWorld, camera.combined);

//        this.shapeRenderer.setProjectionMatrix(camera.combined);
//        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        this.shapeRenderer.setColor(Color.RED);
//        Rectangle attackAABB = AttackSystem.attackAABB;
//        this.shapeRenderer.rect(
//            attackAABB.x,
//            attackAABB.y,
//            attackAABB.width - attackAABB.x,
//            attackAABB.height - attackAABB.y);
//        this.shapeRenderer.end();
    }

    @Override
    public void dispose() {
        this.box2DDebugRenderer.dispose();
        this.shapeRenderer.dispose();
    }
}
