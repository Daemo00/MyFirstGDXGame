package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class MainGame extends Game {

    public Skin skin;
    public BitmapFont font;
    private SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skin/arial.ttf"));
        font = createFont(gen, 16); //16dp == 12pt
        gen.dispose();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.addRegions(new TextureAtlas("skin/uiskin.atlas"));
        this.setScreen(new MainMenuScreen(this));
    }

    private BitmapFont createFont(FreeTypeFontGenerator ftfg, int dp) {
        // Fixing screen size thanks to https://gamedev.stackexchange.com/a/77674/119718
        int scalingFactor = 1;
        switch (Gdx.app.getType()) {
            case Android:
            case iOS:
                // android specific code
                scalingFactor = 1;
                break;
            case Desktop:
                // desktop specific code
                scalingFactor = 2;
                break;
        }
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (dp * Gdx.graphics.getDensity() * scalingFactor);
        Gdx.app.log("INFO", "Density: " + Gdx.graphics.getDensity());
        return ftfg.generateFont(freeTypeFontParameter);
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