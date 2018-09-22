package com.mygdx.game.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.CollisionComponent;
import com.mygdx.game.box2d.entity.components.PlayerComponent;
import com.mygdx.game.box2d.entity.components.StateComponent;
import com.mygdx.game.box2d.entity.components.TextureComponent;
import com.mygdx.game.box2d.entity.components.TransformComponent;
import com.mygdx.game.box2d.entity.components.TypeComponent;
import com.mygdx.game.box2d.entity.systems.AnimationSystem;
import com.mygdx.game.box2d.entity.systems.CollisionSystem;
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
    private final World world;
    private final BodyFactory bodyFactory;
    private final Sound boing;
    private final PooledEngine engine;

    Box2DScreen(MainGame game, MainMenuScreen mainMenuScreen, Box2DAssetManager assetManager) {
        super(game, "Box2D", mainMenuScreen);
        controller = new KeyboardController();
        multiplexer.addProcessor(controller);
        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new Box2DContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        assetManager.queueAddSounds();
        assetManager.manager.finishLoading();
        atlas = assetManager.manager.get("box2d/images/game.atlas", TextureAtlas.class);
        ping = assetManager.manager.get("box2d/sounds/ping.wav", Sound.class);
        boing = assetManager.manager.get("box2d/sounds/boing.wav", Sound.class);
        music = assetManager.manager.get("box2d/music/Rolemusic_-_pl4y1ng.mp3", Music.class);

        sb = new SpriteBatch();
        // Create our new rendering system
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);

        //create a pooled engine
        engine = new PooledEngine();

        // add all the relevant systems our engine should run
        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));

        // create some game objects
        createPlayer();
        createPlatform(2, 2);
        createPlatform(2, 7);
        createPlatform(7, 2);
        createPlatform(7, 7);

        createFloor();

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

    private void createPlayer() {

        // Create the Entity and all the components that will go in the entity
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        b2dbody.body = bodyFactory.makeCirclePolyBody(10, 10, 1, BodyFactory.STONE, BodyDef.BodyType.DynamicBody, true);
        // set object position (x,y,z) z used to define draw order 0 first drawn
        position.position.set(10, 10, 0);
        texture.region = atlas.findRegion("player");
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        // add the components to the entity
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);

        // add the entity to the engine
        engine.addEntity(entity);

    }

    private void createPlatform(float x, float y) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(x, y, 3, 0.2f, BodyFactory.STONE, BodyDef.BodyType.StaticBody, false);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = atlas.findRegion("player");
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);

    }

    private void createFloor() {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(0, 0, 100, 0.2f, BodyFactory.STONE, BodyDef.BodyType.StaticBody, false);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = atlas.findRegion("player");
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;

        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);
    }
}
