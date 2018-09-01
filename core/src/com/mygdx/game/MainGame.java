package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.invaders.screens.GameLoop;
import com.mygdx.game.invaders.screens.GameOver;
import com.mygdx.game.invaders.screens.InvadersScreen;
import com.mygdx.game.invaders.screens.MainMenu;


public class MainGame extends Game {

    public Skin skin;
    public BitmapFont font;
    private SpriteBatch batch;
    /**
     * Music needs to be a class property to prevent being disposed.
     */
    private Music music;
    private FPSLogger fps;

    private Controller controller;
    private ControllerAdapter controllerListener = new ControllerAdapter() {
        @Override
        public void connected(Controller c) {
            if (controller == null) {
                controller = c;
            }
        }

        @Override
        public void disconnected(Controller c) {
            if (controller == c) {
                controller = null;
            }
        }
    };
    private MainMenuScreen mainMenuScreen;

    public Controller getController() {
        return controller;
    }

    public void create() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skin/arial.ttf"));
        font = createFont(gen, 16); //16dp == 12pt
        gen.dispose();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.addRegions(new TextureAtlas("skin/uiskin.atlas"));
        mainMenuScreen = new MainMenuScreen(this);
        this.setScreen(mainMenuScreen);
        Array<Controller> controllers = Controllers.getControllers();
        if (controllers.size > 0) {
            controller = controllers.first();
        }
        Controllers.addListener(controllerListener);
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
        return ftfg.generateFont(freeTypeFontParameter);
    }

    private void renderInvaders() {
        InvadersScreen currentScreen = getInvaderScreen();

        // update the screen
        currentScreen.render(Gdx.graphics.getDeltaTime());

        // When the screen is done we change to the
        // next screen. Ideally the screen transitions are handled
        // in the screen itself or in a proper state machine.
        if (currentScreen.isDone()) {
            // dispose the resources of the current screen
            currentScreen.dispose();

            // if the current screen is a main menu screen we switch to
            // the game loop
            if (currentScreen instanceof MainMenu) {
                setScreen(new GameLoop(this, mainMenuScreen));
            } else {
                // if the current screen is a game loop screen we switch to the
                // game over screen
                if (currentScreen instanceof GameLoop) {
                    setScreen(new GameOver(this, mainMenuScreen));
                } else if (currentScreen instanceof GameOver) {
                    // if the current screen is a game over screen we switch to the
                    // main menu screen
                    setScreen(new MainMenu(this, mainMenuScreen));
                }
            }
        }

        // fps.log();
    }

    /**
     * For this game each of our screens is an instance of InvadersScreen.
     *
     * @return the currently active {@link InvadersScreen}.
     */
    private InvadersScreen getInvaderScreen() {
        return (InvadersScreen) super.getScreen();
    }

    public void render() {
        super.render(); //important!
        if (getScreen() instanceof InvadersScreen) {
            InvadersScreen invadersScreen = (InvadersScreen) getScreen();
            renderInvaders();
        }
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        skin.dispose();
        screen.dispose();
    }
}