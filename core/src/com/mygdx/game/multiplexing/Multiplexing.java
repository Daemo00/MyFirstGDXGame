package com.mygdx.game.multiplexing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class Multiplexing extends GenericGameScreen {

    private OrthographicCamera camera;

    private final Slider slider;


    public Multiplexing(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Multiplexing", mainMenuScreen);

        Image map = new Image(new Texture("camera/map.jpg"));
        map.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);
                camera.position.x -= (deltaX * Gdx.graphics.getDensity());
                camera.position.y -= (deltaY * Gdx.graphics.getDensity());

            }
        });
        gameStage.addActor(map);

        Image ring = new Image((new Texture("faces/Dinoface.png")));
        ring.setPosition(1100, 1225);
        gameStage.addActor(ring);

        Image magnifier = new Image(new Texture("magnifier.png"));
        magnifier.setPosition(Gdx.graphics.getWidth() / 2 - magnifier.getWidth() / 4, Gdx.graphics.getHeight() / 2 - magnifier.getHeight() / 2);
        HUDStage.addActor(magnifier);

        slider = new Slider(1, 2, 0.01f, true, game.skin);
        slider.setAnimateInterpolation(Interpolation.smooth);
        slider.setHeight(Gdx.graphics.getHeight() * 0.8f);
        slider.setPosition(Gdx.graphics.getWidth() / 12, Gdx.graphics.getHeight() / 10);
        slider.setValue(1.55f);
        slider.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                camera.zoom = slider.getValue();
                camera.update();
                super.touchDragged(event, x, y, pointer);
            }
        });
        HUDStage.addActor(slider);

        camera = (OrthographicCamera) gameStage.getViewport().getCamera();
        camera.translate(200, 250);
        camera.zoom = 1.55f;
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
}
