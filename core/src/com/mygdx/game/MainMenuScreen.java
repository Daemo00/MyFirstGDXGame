package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.action.ActionScreen;
import com.mygdx.game.drop.GameScreen;

public class MainMenuScreen implements Screen {

    private final Stage stage;
    private final int col_width;
    private final int row_height;
    private final Skin mySkin;

    MainMenuScreen(final MainGame game) {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;

        mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        stage.addActor(createLabel());
        stage.addActor(createActionButton(game));
        stage.addActor(createDropButton(game));
    }

    private Label createLabel() {
        Label title = new Label("My games", mySkin);
        title.setSize(Gdx.graphics.getWidth(), row_height * 2);
        title.setPosition(0, Gdx.graphics.getHeight() - row_height * 2);
        title.setAlignment(Align.center);
        return title;
    }

    private Button createDropButton(final MainGame game) {
        Button dropGameButton = new TextButton("Drops game", mySkin, "small");
        dropGameButton.setSize(col_width * 4, row_height);
        dropGameButton.setPosition(col_width, Gdx.graphics.getHeight() - row_height * 3);
        dropGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
        return dropGameButton;
    }
    private Button createActionButton(final MainGame game) {
        Button dropGameButton = new TextButton("Actions game", mySkin, "small");
        dropGameButton.setSize(col_width * 4, row_height);
        dropGameButton.setPosition(col_width * 7, Gdx.graphics.getHeight() - row_height * 3);
        dropGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ActionScreen());
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
        mySkin.dispose();
        stage.dispose();
    }
}