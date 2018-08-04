package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.action.ActionScreen;
import com.mygdx.game.drop.DropScreen;

public class MainMenuScreen implements Screen {

    private final Stage stage;
    public static int col_width;
    public static int row_height;
    private MainGame game;

    MainMenuScreen(final MainGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;

        stage.addActor(createLabel());
        stage.addActor(createActionButton(game));
        stage.addActor(createDropButton(game));
    }

    private Label createLabel() {
        Label title = new Label("My games", game.skin);
        title.setSize(Gdx.graphics.getWidth(), row_height * 2);
        title.setPosition(0, Gdx.graphics.getHeight() - row_height * 2);
        title.setAlignment(Align.center);
        return title;
    }

    private Button createDropButton(final MainGame game) {
        Button dropGameButton = new TextButton("Drops game", game.skin, "small");
        dropGameButton.setSize(col_width * 4, row_height);
        dropGameButton.setPosition(col_width, Gdx.graphics.getHeight() - row_height * 3);
        dropGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new DropScreen(game));
                return true;
            }
        });
        return dropGameButton;
    }
    private Button createActionButton(final MainGame game) {
        Button dropGameButton = new TextButton("Actions game", game.skin, "small");
        dropGameButton.setSize(col_width * 4, row_height);
        dropGameButton.setPosition(col_width * 7, Gdx.graphics.getHeight() - row_height * 3);
        dropGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ActionScreen(game));
                return true;
            }
        });
        return dropGameButton;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
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