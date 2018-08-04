package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.mygdx.game.MainMenuScreen.col_width;
import static com.mygdx.game.MainMenuScreen.row_height;

public class GenericGameScreen implements Screen {

    protected MainGame game;
    protected Stage stage;

    public GenericGameScreen(MainGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        stage.addActor(createBackButton());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    private Button createBackButton() {
        Button backGameButton = new TextButton("Back", game.skin);
        backGameButton.setSize(col_width, row_height);
        backGameButton.setPosition(Gdx.graphics.getWidth() - col_width, Gdx.graphics.getHeight() - row_height);
        backGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });
        return backGameButton;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
        stage.dispose();
    }
}
