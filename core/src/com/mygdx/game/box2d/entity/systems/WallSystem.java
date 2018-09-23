package com.mygdx.game.box2d.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.WallComponent;

public class WallSystem extends IteratingSystem {
    private Entity player;
    private ComponentMapper<Box2DBodyComponent> bm = ComponentMapper.getFor(Box2DBodyComponent.class);

    public WallSystem(Entity player) {
        super(Family.all(WallComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get current y level of player entity
        float currentyLevel = player.getComponent(Box2DBodyComponent.class).body.getPosition().y;
        // get the body component of the wall we're updating
        Body bod = bm.get(entity).body;
        //set the walls y position to match the player
        bod.setTransform(bod.getPosition().x, currentyLevel, bod.getAngle());
    }
}