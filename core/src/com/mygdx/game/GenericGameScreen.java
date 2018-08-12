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
    protected String title;
    private Stage HUDStage;
    private MainMenuScreen mainMenuScreen;

    public GenericGameScreen(MainGame game, String title, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.title = title;
        this.mainMenuScreen = mainMenuScreen;

        stage = new Stage(new ScreenViewport());
        HUDStage = new Stage(new ScreenViewport());
        HUDStage.addActor(createBackButton());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(HUDStage);
    }

    private Button createBackButton() {
        TextButton backGameButton = new TextButton("Back", game.skin);
        backGameButton.setSize(col_width, row_height);
        backGameButton.setPosition(Gdx.graphics.getWidth() - col_width, Gdx.graphics.getHeight() - row_height);
        Gdx.app.log("DBG", "Button " + backGameButton.getText() + " label height: " + backGameButton.getLabel().getHeight());
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = this.game.skin.getDrawable("default-rect");
        style.down = this.game.skin.getDrawable("default-rect-down");
        style.font = this.game.skin.getFont("default-font");
        backGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(mainMenuScreen);
                return true;
            }
        });
        return backGameButton;
    }

    protected void preRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {

    }

    protected void postRender() {
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
        stage.dispose();
    }
}
