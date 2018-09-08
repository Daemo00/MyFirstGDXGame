package com.mygdx.game.parallax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.actors.ParallaxBackground;


public class ParallaxScreen extends GenericGameScreen {

    public ParallaxScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Parallax", mainMenuScreen);

        Array<Texture> textures = new Array<Texture>();
        for (int i = 1; i <= 6; i++) {
            textures.add(new Texture(Gdx.files.internal("parallax/img" + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1);
        gameStage.addActor(parallaxBackground);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
