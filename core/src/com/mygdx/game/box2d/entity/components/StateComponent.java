package com.mygdx.game.box2d.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StateComponent implements Component, Pool.Poolable {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_JUMPING = 1;
    public static final int STATE_FALLING = 2;
    public static final int STATE_MOVING = 3;
    public static final int STATE_HIT = 4;
    public float time = 0.0f;
    public boolean isLooping = false;
    private int state = 0;

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }

    public int get() {
        return state;
    }

    @Override
    public void reset() {
        time = 0.0f;
        isLooping = false;
        state = 0;
    }
}