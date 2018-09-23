package com.mygdx.game.box2d.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    public IntMap<Animation<TextureRegion>> animations = new IntMap<Animation<TextureRegion>>();

    @Override
    public void reset() {

    }
}