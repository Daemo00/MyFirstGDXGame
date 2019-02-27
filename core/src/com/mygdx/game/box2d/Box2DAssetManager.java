package com.mygdx.game.box2d;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

class Box2DAssetManager {

    final AssetManager manager = new AssetManager();
    final String loadingImages = "box2d/images/loading.atlas";
    final String floorImage = "box2d/images/reallybadlydrawndirt.png";
    final String waterImage = "box2d/images/water.png";
    // Sounds
    private final String boingSound = "box2d/sounds/boing.wav";
    private final String pingSound = "box2d/sounds/ping.wav";
    // Music
    private final String playingSong = "box2d/music/Rolemusic_-_pl4y1ng.mp3";
    // Textures
    private final String gameImages = "box2d/images/game.atlas";

    void queueAddSounds() {
        manager.load(boingSound, Sound.class);
        manager.load(pingSound, Sound.class);
    }

    void queueAddMusic() {
        manager.load(playingSong, Music.class);
    }

    void queueAddGameImages() {
        manager.load(gameImages, TextureAtlas.class);
        manager.load(floorImage, Texture.class);
        manager.load(waterImage, Texture.class);
    }

    // a small set of images used by the loading screen
    void queueAddLoadingImages() {
        manager.load(loadingImages, TextureAtlas.class);
    }

    void queueAddFonts() {

    }

    void queueAddParticleEffects() {

    }
}
