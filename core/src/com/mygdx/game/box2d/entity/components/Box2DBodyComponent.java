package com.mygdx.game.box2d.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class Box2DBodyComponent implements Component, Pool.Poolable {
    public Body body;
    public boolean isDead;

    @Override
    public void reset() {
        body = null;
        isDead = false;
    }
}