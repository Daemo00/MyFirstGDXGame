package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private int scroll;
    private final Array<Texture> layers;

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float scaleX;
    private final float scaleY;
    private final int originX;
    private final int originY;
    private final int rotation;
    private final int srcY;
    private final boolean flipX;
    private final boolean flipY;

    private int speed;

    public ParallaxBackground(Array<Texture> textures) {
        layers = textures;
        for (int i = 0; i < textures.size; i++) {
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        scroll += speed;
        for (int i = 0; i < layers.size; i++) {
            int LAYER_SPEED_DIFFERENCE = 2;
            int srcX = scroll + i * LAYER_SPEED_DIFFERENCE * scroll;
            batch.draw(layers.get(i), x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, layers.get(i).getWidth(), layers.get(i).getHeight(), flipX, flipY);
        }
    }
}