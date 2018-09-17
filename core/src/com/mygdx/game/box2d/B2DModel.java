package com.mygdx.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class B2DModel {
    private final Body player;
    public World world;
    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    public boolean isSwimming = false;

    B2DModel() {
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
        player = bodyFactory.makeCirclePolyBody(1, 1, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody);

        // add some water
        Body water = bodyFactory.makeCirclePolyBody(1, -8, 40, BodyFactory.RUBBER, BodyDef.BodyType.StaticBody);
        water.setUserData("IAMTHESEA");
        // make the water a sensor so it doesn't obstruct our player
        bodyFactory.makeAllFixturesSensors(water);
    }

    // our game logic here
    public void logicStep(float delta) {
        if (isSwimming) {
            player.applyForceToCenter(0, 40, true);
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
}