package com.mygdx.game.box2d;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class Box2DScreen extends GenericGameScreen {
    private final Box2DDebugRenderer debugRenderer;
    private final B2DModel model;
    private final OrthographicCamera cam;
    private final KeyboardController controller;
    private final TextureAtlas.AtlasRegion playerTex;
    private final SpriteBatch sb;
    private final Music playingSong;
    private final TextureAtlas atlas;

    Box2DScreen(MainGame game, MainMenuScreen mainMenuScreen, Box2DAssetManager assetManager) {
        super(game, "Box2D", mainMenuScreen);
        cam = new OrthographicCamera(32, 24);
        controller = new KeyboardController();
        multiplexer.addProcessor(controller);
        model = new B2DModel(controller, cam, assetManager);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        // tells our asset manger that we want to load the images set in loadImages method
        assetManager.queueAddGameImages();
        // tells the asset manager to load the images and wait until finished loading.
        assetManager.manager.finishLoading();
        // gets the images as a texture
        atlas = assetManager.manager.get(assetManager.gameImages);
        playerTex = atlas.findRegion("player");
        sb = new SpriteBatch();
        sb.setProjectionMatrix(cam.combined);

        // tells our asset manger that we want to load the images set in loadImages method
        assetManager.queueAddMusic();
        // tells the asset manager to load the images and wait until finished loading.
        assetManager.manager.finishLoading();
        // loads the 2 sounds we use
        playingSong = assetManager.manager.get(assetManager.playingSong);
    }

    @Override
    public void show() {
        super.show();
        playingSong.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        debugRenderer.render(model.world, cam.combined);
        model.logicStep(delta);
        sb.begin();
        sb.draw(playerTex, model.player.getPosition().x - 1, model.player.getPosition().y - 1, 2, 2);
        sb.end();
    }

    @Override
    public void hide() {
        super.hide();
        playingSong.pause();
    }
}
