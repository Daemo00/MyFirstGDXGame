package com.mygdx.game.box2d.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.TransformComponent;


public class PhysicsSystem extends IteratingSystem {

    private World world;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<Box2DBodyComponent> bm = ComponentMapper.getFor(Box2DBodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    public PhysicsSystem(World world) {
        super(Family.all(Box2DBodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(deltaTime, 6, 2);

        //Entity Queue
        for (Entity entity : bodiesQueue) {
            TransformComponent tfm = tm.get(entity);
            Box2DBodyComponent bodyComp = bm.get(entity);
            Vector2 position = bodyComp.body.getPosition();
            tfm.position.x = position.x;
            tfm.position.y = position.y;
            tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
            if (bodyComp.isDead) {
                System.out.println("Removing a corpse");
                world.destroyBody(bodyComp.body);
                getEngine().removeEntity(entity);
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}