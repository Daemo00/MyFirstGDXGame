package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MainGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        screen.dispose();
    }
}