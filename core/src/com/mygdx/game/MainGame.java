package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MainGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        screen = new MainMenuScreen(this);
        this.setScreen(screen);
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