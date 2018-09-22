package com.mygdx.game.box2d;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.box2d.entity.systems.AnimationSystem;
import com.mygdx.game.box2d.entity.systems.CollisionSystem;
import com.mygdx.game.box2d.entity.systems.LevelGenerationSystem;
import com.mygdx.game.box2d.entity.systems.PhysicsDebugSystem;
import com.mygdx.game.box2d.entity.systems.PhysicsSystem;
import com.mygdx.game.box2d.entity.systems.PlayerControlSystem;
import com.mygdx.game.box2d.entity.systems.RenderingSystem;

public class Box2DScreen extends GenericGameScreen {
    private final Music music;
    private final OrthographicCamera cam;
    private final KeyboardController controller;
    private final SpriteBatch sb;
    private final TextureAtlas atlas;
    private final Sound ping;
    private final Sound boing;
    private final PooledEngine engine;
    private final LevelFactory lvlFactory;

    Box2DScreen(MainGame game, MainMenuScreen mainMenuScreen, Box2DAssetManager assetManager) {
        super(game, "Box2D", mainMenuScreen);
        assetManager.queueAddSounds();
        assetManager.manager.finishLoading();
        atlas = assetManager.manager.get("box2d/images/game.atlas", TextureAtlas.class);
        ping = assetManager.manager.get("box2d/sounds/ping.wav", Sound.class);
        boing = assetManager.manager.get("box2d/sounds/boing.wav", Sound.class);
        music = assetManager.manager.get("box2d/music/Rolemusic_-_pl4y1ng.mp3", Music.class);
        controller = new KeyboardController();
        multiplexer.addProcessor(controller);
        engine = new PooledEngine();
        lvlFactory = new LevelFactory(engine, atlas.findRegion("player"));


        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(lvlFactory.world));
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));

        lvlFactory.createPlayer(atlas.findRegion("player"), cam);
        lvlFactory.createFloor(atlas.findRegion("player"));
    }

    @Override
    public void show() {
        super.show();
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);
    }

    @Override
    public void hide() {
        super.hide();
        music.pause();
    }
}
