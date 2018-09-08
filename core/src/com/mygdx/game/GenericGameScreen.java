package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class GenericGameScreen implements Screen {

    protected final MainGame game;
    protected final Stage gameStage;
    protected final MainMenuScreen mainMenuScreen;
    protected final Stage HUDStage;
    final String title;
    private final MyInputMultiplexer multiplexer;


    protected GenericGameScreen(MainGame game, String title, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.title = title;
        this.mainMenuScreen = mainMenuScreen;

        multiplexer = new MyInputMultiplexer();
        multiplexer.addProcessor(HUDStage = new Stage());
        multiplexer.addProcessor(gameStage = new Stage());
        HUDStage.addActor(createBackButton());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    private Button createBackButton() {
        TextButton backGameButton = new TextButton("Back", mainMenuScreen.buttonStyle);
        backGameButton.getLabel().setWrap(true);
        backGameButton.setPosition(
                Gdx.graphics.getWidth() - backGameButton.getWidth(),
                Gdx.graphics.getHeight() - backGameButton.getHeight());
        backGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                backAction();
            }
        });
        return backGameButton;
    }

    private void backAction() {
        game.setScreen(mainMenuScreen);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            backAction();
        renderStages();
    }

    protected void renderStages() {
        HUDStage.act();
        gameStage.act();

        gameStage.draw();
        HUDStage.draw();
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
        HUDStage.dispose();
        gameStage.dispose();
    }
}
