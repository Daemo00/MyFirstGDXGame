package com.mygdx.game.box2d.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerComponent implements Component {

    public OrthographicCamera cam;
    public boolean onPlatform;
    public boolean isDead;
    public boolean onSpring;
}