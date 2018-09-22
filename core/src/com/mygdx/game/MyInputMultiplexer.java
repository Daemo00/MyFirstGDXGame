package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;

public class MyInputMultiplexer extends InputMultiplexer {

    @Override
    public boolean keyDown(int keyCode) {
        Gdx.app.log("INPUT", Input.Keys.toString(keyCode));
        return super.keyDown(keyCode);
    }
}
