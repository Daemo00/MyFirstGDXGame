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
import com.mygdx.game.camera.CameraScreen;
import com.mygdx.game.drop.DropScreen;

import java.util.ArrayList;

public class MainMenuScreen implements Screen {

    private final Stage stage;
    private static int currX;
    public static int col_width;
    public static int row_height;
    private MainGame game;

    MainMenuScreen(final MainGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        row_height = stage.getViewport().getScreenWidth() / 12;
        col_width = stage.getViewport().getScreenWidth() / 12;

        stage.addActor(createLabel());

        ArrayList<GenericGameScreen> gameScreens = new ArrayList<GenericGameScreen>();
        gameScreens.add(new ActionScreen(game, this));
        gameScreens.add(new DropScreen(game, this));
        gameScreens.add(new CameraScreen(game, this));

        for (GenericGameScreen gameScreen : gameScreens) {
            stage.addActor(createGameButton(gameScreen));
            currX += col_width;
        }
        currX = 0;
    }

    private Label createLabel() {
        Label title = new Label("My games", game.skin);
        title.setSize(stage.getViewport().getScreenWidth(), row_height * 2);
        title.setPosition(0, Gdx.graphics.getHeight() - row_height * 2);
        title.setAlignment(Align.center);
        return title;
    }

    private Button createGameButton(final GenericGameScreen gameScreen) {
        TextButton dropGameButton = new TextButton(gameScreen.title, this.game.skin);
        dropGameButton.setSize(col_width, row_height);
        dropGameButton.setPosition(currX + col_width, Gdx.graphics.getHeight() - row_height * 3);
        Gdx.app.log("DBG", "Button " + dropGameButton.getText() + " label height: " + dropGameButton.getLabel().getHeight());
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = this.game.skin.getDrawable("default-rect");
        style.down = this.game.skin.getDrawable("default-rect-down");
        style.font = this.game.skin.getFont("default-font");
        dropGameButton.setStyle(style);
        dropGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(gameScreen);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        return dropGameButton;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
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