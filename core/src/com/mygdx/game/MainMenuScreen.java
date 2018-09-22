package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.action.ActionScreen;
import com.mygdx.game.box2d.Box2DLoadingScreen;
import com.mygdx.game.camera.CameraScreen;
import com.mygdx.game.cubocy.screens.CubocyMenu;
import com.mygdx.game.drop.DropScreen;
import com.mygdx.game.invaders.screens.InvadersMenu;
import com.mygdx.game.multiplexing.Multiplexing;
import com.mygdx.game.parallax.ParallaxScreen;

import java.util.ArrayList;

public class MainMenuScreen implements Screen {

    private final Stage stage;
    private final MainGame game;
    TextButton.TextButtonStyle buttonStyle;
    Label.LabelStyle labelStyle;

    MainMenuScreen(final MainGame game) {
        this.game = game;
        stage = new Stage();

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = this.game.skin.getDrawable("button");
        buttonStyle.down = this.game.skin.getDrawable("button-down");
        buttonStyle.over = this.game.skin.getDrawable("button-over");
        buttonStyle.font = this.game.font;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = this.game.font;

        ArrayList<Screen> gameScreens = new ArrayList<Screen>();
        gameScreens.add(new ActionScreen(game, this));
        gameScreens.add(new DropScreen(game, this));
        gameScreens.add(new CameraScreen(game, this));
        gameScreens.add(new ParallaxScreen(game, this));
        gameScreens.add(new Multiplexing(game, this));
        gameScreens.add(new InvadersMenu(game, this));
        gameScreens.add(new CubocyMenu(game, this));
        gameScreens.add(new PreferencesScreen(game, this));
        gameScreens.add(new Box2DLoadingScreen(game, this));

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);

        table.add(createTitleLabel());
        table.row().padTop(50);

        Button b;
        for (Screen gameScreen : gameScreens) {
            b = createGameButton(gameScreen);
            table.add(b).width(150);
            table.row().padTop(10);
        }
        stage.addActor(table);
    }

    private Label createTitleLabel() {
        Label title = new Label("My games", game.skin);
        title.setFontScale(2);
        title.setStyle(labelStyle);
        title.setAlignment(Align.center);
        return title;
    }

    private Button createGameButton(final Screen gameScreen) {
        TextButton gameButton;
        if (gameScreen instanceof GenericGameScreen) {
            GenericGameScreen genericGameScreen = (GenericGameScreen) gameScreen;
            gameButton = new TextButton(genericGameScreen.title, this.game.skin);
        } else {
            Gdx.app.log("ERROR", "Screen should be instance of GenericGameScreen");
            return null;
        }

        gameButton.setStyle(buttonStyle);
        gameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
            }
        });
        gameButton.getLabel().setWrap(true);
        return gameButton;
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