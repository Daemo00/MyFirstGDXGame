package com.mygdx.game.box2d.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.BulletComponent;

public class BulletSystem extends IteratingSystem {

    private final ComponentMapper<BulletComponent> bm;
    private final Entity player;
    private ComponentMapper<Box2DBodyComponent> bxm;

    @SuppressWarnings("unchecked")
    public BulletSystem(Entity player) {
        super(Family.all(BulletComponent.class).get());
        bm = ComponentMapper.getFor(BulletComponent.class);
        bxm = ComponentMapper.getFor(Box2DBodyComponent.class);
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        Box2DBodyComponent b2body = bxm.get(entity);
        BulletComponent bullet = bm.get(entity);

        // apply bullet velocity to bullet body
        b2body.body.setLinearVelocity(bullet.xVel, bullet.yVel);

        // get player pos
        Box2DBodyComponent playerBodyComp = bxm.get(player);
        float px = playerBodyComp.body.getPosition().x;
        float py = playerBodyComp.body.getPosition().y;

        //get bullet pos
        float bx = b2body.body.getPosition().x;
        float by = b2body.body.getPosition().y;

        // if bullet is 20 units away from player on any axis then it is probably off screen
        if (bx - px > 20 || by - py > 20) {
            bullet.isDead = true;
        }

        //check if bullet is dead
        if (bullet.isDead) {
            System.out.println("Bullet died");
            b2body.isDead = true;
        }
    }
}