package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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

    protected final MainGame game;
    protected final Stage stage;
    protected final MainMenuScreen mainMenuScreen;
    protected final InputMultiplexer multiplexer;
    final String title;


    protected GenericGameScreen(MainGame game, String title, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.title = title;
        this.mainMenuScreen = mainMenuScreen;

        stage = new MyStage(new ScreenViewport());
        stage.addActor(createBackButton());
        Gdx.input.setCatchBackKey(true);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    private Button createBackButton() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = this.game.skin.getDrawable("button");
        style.down = this.game.skin.getDrawable("button-down");
        style.font = this.game.font;
        TextButton backGameButton = new TextButton("Back", style);
        backGameButton.setSize(col_width, row_height);
        backGameButton.setPosition(Gdx.graphics.getWidth() - col_width, Gdx.graphics.getHeight() - row_height);
        backGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backAction();
                return true;
            }
        });
        backGameButton.getLabel().setWrap(true);
        return backGameButton;
    }

    private void backAction() {
        game.setScreen(mainMenuScreen);
    }

    protected void preRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            backAction();
    }

    @Override
    public void render(float delta) {

    }

    protected void postRender() {
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
