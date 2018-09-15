package com.mygdx.game.box2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class Box2DScreen extends GenericGameScreen {
    private final Box2DDebugRenderer debugRenderer;
    private final B2DModel model;
    private final OrthographicCamera cam;

    public Box2DScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Box2D", mainMenuScreen);
        model = new B2DModel();
        cam = new OrthographicCamera(32, 24);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        debugRenderer.render(model.world, cam.combined);
        model.logicStep(delta);
    }
}
