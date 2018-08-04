package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class MainGame extends Game {

    public Skin skin;
    public BitmapFont font;
    private SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        skin.dispose();
        screen.dispose();
    }
}