package com.mygdx.game.box2d.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.EnemyComponent;

public class EnemySystem extends IteratingSystem {

    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<Box2DBodyComponent> bodm;

    public EnemySystem() {
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(Box2DBodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = em.get(entity);        // get EnemyComponent
        Box2DBodyComponent bodyCom = bodm.get(entity);    // get B2dBodyComponent

        // get distance of enemy from its original start position (pad center)
        float distFromOrig = Math.abs(enemyCom.xPosCenter - bodyCom.body.getPosition().x);

        // if distance > 1 swap direction
        enemyCom.isGoingLeft = (distFromOrig > 1) != enemyCom.isGoingLeft;

        // set speed base on direction
        float speed = enemyCom.isGoingLeft ? -0.01f : 0.01f;

        // apply speed to body
        bodyCom.body.setTransform(bodyCom.body.getPosition().x + speed,
                bodyCom.body.getPosition().y,
                bodyCom.body.getAngle());

        if (enemyCom.isDead) {
            bodyCom.isDead = true;
        }
    }
}