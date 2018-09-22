package com.mygdx.game.box2d;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class B2DModel {
    static final int BOING_SOUND = 0;
    private static final int PING_SOUND = 1;
    final Body player;
    private final Sound ping;
    private final Sound boing;
    public World world;
    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    public boolean isSwimming = false;
    private KeyboardController controller;
    private OrthographicCamera camera;
    private Box2DAssetManager assetManager;

    B2DModel(KeyboardController controller, OrthographicCamera cam, Box2DAssetManager assetManager) {
        this.controller = controller;
        camera = cam;
        this.assetManager = assetManager;
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new Box2DContactListener(this));
        createFloor();
        /*createObject();
        createMovingObject();

        // get our body factory singleton and store it in bodyFactory
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        // add a new rubber ball at position 1, 1
        bodyFactory.makeCirclePolyBody(1, 1, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody);

        // add a new steel ball at position 4, 1
        bodyFactory.makeCirclePolyBody(4, 1, 2, BodyFactory.STEEL, BodyDef.BodyType.DynamicBody);

        // add a new stone at position -4,1
        bodyFactory.makeCirclePolyBody(-4, 1, 2, BodyFactory.STONE, BodyDef.BodyType.DynamicBody);

        bodyFactory.makePolygonShapeBody(3, 5, 3, BodyFactory.WOOD, BodyDef.BodyType.DynamicBody);

        bodyFactory.makePolygonShapeBody(6, -1, 3, BodyFactory.WOOD, BodyDef.BodyType.DynamicBody);

        Body p = bodyFactory.makePolygonShapeBody(4, -8, 3, BodyFactory.WOOD, BodyDef.BodyType.DynamicBody);

        bodyFactory.makeConeSensor(p, 2);
        */
        world.setContactListener(new Box2DContactListener(this));
        createFloor();
        //createObject();
        //createMovingObject();

        // get our body factory singleton and store it in bodyFactory
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        // add a player
        player = bodyFactory.makeCirclePolyBody(0, 0, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody);

        // add some water
        Body water = bodyFactory.makeBoxBody(0, 0, 40, 2, BodyFactory.RUBBER, BodyDef.BodyType.StaticBody, false);
        water.setUserData("IAMTHESEA");
        // make the water a sensor so it doesn't obstruct our player
        bodyFactory.makeAllFixturesSensors(water);

        assetManager.queueAddSounds();
        assetManager.manager.finishLoading();
        ping = assetManager.manager.get(assetManager.pingSound, Sound.class);
        boing = assetManager.manager.get(assetManager.boingSound, Sound.class);
    }

    // our game logic here
    public void logicStep(float delta) {
        if (controller.left) {
            player.applyForceToCenter(-10, 0, true);
        } else if (controller.right) {
            player.applyForceToCenter(10, 0, true);
        } else if (controller.up) {
            player.applyForceToCenter(0, 10, true);
        } else if (controller.down) {
            player.applyForceToCenter(0, -10, true);
        }
        if (isSwimming) {
            player.applyForceToCenter(0, 40, true);
        }

        // check if mouse1 is down (player click) then if true check if point intersects
        if (controller.isMouse1Down && pointIntersectsBody(player, controller.mouseLocation)) {
            System.out.println("Player was clicked");
        }

        world.step(delta, 3, 3);
    }

    private void createObject() {

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);


        // add it to the world
        bodyd = world.createBody(bodyDef);

        // set the shape (here we use a box 1 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }

    private void createFloor() {
        // create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, -10);
        // add it to the world
        bodys = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 1);
        // create the physical object in our body)
        // without this our body would just be data in the world
        bodys.createFixture(shape, 0.0f);
        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }

    private void createMovingObject() {

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0, -10);


        // add it to the world
        bodyk = world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2, 1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyk.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        bodyk.setLinearVelocity(0, 0.75f);
    }

    private boolean pointIntersectsBody(Body body, Vector2 mouseLocation) {
        Vector3 mousePos = new Vector3(mouseLocation, 0); //convert mouseLocation to 3D position
        camera.unproject(mousePos); // convert from screen position to world position
        return body.getFixtureList().first().testPoint(mousePos.x, mousePos.y);
    }

    public void playSound(int sound) {
        switch (sound) {
            case BOING_SOUND:
                boing.play();
                break;
            case PING_SOUND:
                ping.play();
                break;
        }
    }
}