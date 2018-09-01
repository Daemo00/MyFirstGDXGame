
package com.mygdx.game.cubocy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class GameOverScreen extends CubocScreen {
    private TextureRegion intro;
    private SpriteBatch batch;
    private float time = 0;

    GameOverScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, mainMenuScreen);
    }

    @Override
    public void show() {
        super.show();
        intro = new TextureRegion(new Texture(Gdx.files.internal("cubocy/gameover.png")), 0, 0, 480, 320);
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render(float delta) {
        preRender();
        batch.begin();
        batch.draw(intro, 0, 0);
        batch.end();

        time += delta;
        if (time > 1) {
            if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
                game.setScreen(new CubocyMenu(game, mainMenuScreen));
            }
        }
        postRender();
    }

    @Override
    public void hide() {
        Gdx.app.debug("Cubocy", "dispose intro");
        batch.dispose();
        intro.getTexture().dispose();
    }
}
