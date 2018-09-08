package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.invaders.screens.GameLoop;
import com.mygdx.game.invaders.screens.GameOver;
import com.mygdx.game.invaders.screens.InvadersMenu;
import com.mygdx.game.invaders.screens.InvadersScreen;


public class MainGame extends Game {

    public Skin skin;
    public BitmapFont font;
    private Controller controller;
    private final ControllerAdapter controllerListener = new ControllerAdapter() {
        @Override
        public void connected(Controller c) {
            if (controller == null) controller = c;
        }

        @Override
        public void disconnected(Controller c) {
            if (controller == c) controller = null;
        }
    };
    private MainMenuScreen mainMenuScreen;

    public Controller getController() {
        return controller;
    }

    public void create() {
        createFont();
        createSkin();
        manageControllers();
        Gdx.input.setCatchBackKey(true);
        this.setScreen(mainMenuScreen = new MainMenuScreen(this));
    }

    private void manageControllers() {
        Array<Controller> controllers = Controllers.getControllers();
        if (controllers.size > 0) controller = controllers.first();
        Controllers.addListener(controllerListener);
    }

    private void createSkin() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.addRegions(new TextureAtlas("skin/uiskin.atlas"));
    }

    private void createFont() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skin/arial.ttf"));
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
        freeTypeFontParameter.size = (int) (16 * Gdx.graphics.getDensity() * scalingFactor);
        font = gen.generateFont(freeTypeFontParameter);
        gen.dispose();
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
            if (currentScreen instanceof InvadersMenu) {
                setScreen(new GameLoop(this, mainMenuScreen));
            } else {
                // if the current screen is a game loop screen we switch to the
                // game over screen
                if (currentScreen instanceof GameLoop) {
                    setScreen(new GameOver(this, mainMenuScreen));
                } else if (currentScreen instanceof GameOver) {
                    // if the current screen is a game over screen we switch to the
                    // main menu screen
                    setScreen(new InvadersMenu(this, mainMenuScreen));
                }
            }
        }
    }

    private InvadersScreen getInvaderScreen() {
        return (InvadersScreen) super.getScreen();
    }

    public void render() {
        super.render(); //important!
        if (getScreen() instanceof InvadersScreen) {
            renderInvaders();
        }
    }

    public void dispose() {
        super.dispose();
        font.dispose();
        skin.dispose();
        screen.dispose();
    }
}