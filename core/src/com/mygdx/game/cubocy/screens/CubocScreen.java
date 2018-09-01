
package com.mygdx.game.cubocy.screens;

import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public abstract class CubocScreen extends GenericGameScreen {
    MainGame game;

    CubocScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Cubocy", mainMenuScreen);
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        preRender();
        postRender();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
