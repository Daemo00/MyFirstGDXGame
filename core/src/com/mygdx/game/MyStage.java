package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

class MyStage extends Stage {
    MyStage(ScreenViewport screenViewport) {
        super(screenViewport);
    }

    @Override
    public boolean keyDown(int keyCode) {
        Gdx.app.log("INPUT", Input.Keys.toString(keyCode));
        return super.keyDown(keyCode);
    }
}
