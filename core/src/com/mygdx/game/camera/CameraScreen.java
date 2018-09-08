package com.mygdx.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class CameraScreen extends GenericGameScreen {
    private final int startX = 1100;// -Gdx.graphics.getWidth()/2;
    private final int startY = 1225;
    private final OrthographicCamera camera;
    private float percent;
    private final long startTime;

    public CameraScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Camera", mainMenuScreen);
        gameStage.addActor(new Image(new Texture("camera/map.jpg")));
        camera = (OrthographicCamera) gameStage.getViewport().getCamera();
        camera.translate(startX, startY);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        long secondFromStart = System.currentTimeMillis() - startTime;
        float animation_duration = 15000;
        percent = (secondFromStart % animation_duration) / animation_duration;
        percent = (float) Math.cos(percent * Math.PI * 2) / 2 + 0.5f;
        moveCamera();
    }

    private void moveCamera() {
        int endX = 2350;
        float currentX = startX + (endX - startX) * percent;
        int endY = 600;
        float currentY = startY + (endY - startY) * percent;
        float percentZ = Math.abs(percent - 0.5f) * 2;
        float maxAltitude = 2.5f;
        float minAltitude = 0.5f;
        float currentZ = maxAltitude - (maxAltitude - minAltitude) * percentZ;

        camera.position.x = currentX;
        camera.position.y = currentY;
        camera.zoom = currentZ;
        camera.update();
    }
}
