package com.mygdx.game.box2d;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Box2DAssetManager {

    public final AssetManager manager = new AssetManager();

    // Sounds
    public final String boingSound = "box2d/sounds/boing.wav";
    public final String pingSound = "box2d/sounds/ping.wav";
    // Music
    public final String playingSong = "box2d/music/Rolemusic_-_pl4y1ng.mp3";
    // Textures
    public final String gameImages = "box2d/images/game.atlas";
    public final String loadingImages = "box2d/images/loading.atlas";

    public void queueAddSounds() {
        manager.load(boingSound, Sound.class);
        manager.load(pingSound, Sound.class);
    }

    public void queueAddMusic() {
        manager.load(playingSong, Music.class);
    }

    public void queueAddGameImages() {
        manager.load(gameImages, TextureAtlas.class);
    }

    // a small set of images used by the loading screen
    public void queueAddLoadingImages() {
        manager.load(loadingImages, TextureAtlas.class);
    }

    public void queueAddFonts() {

    }

    public void queueAddParticleEffects() {

    }
}
