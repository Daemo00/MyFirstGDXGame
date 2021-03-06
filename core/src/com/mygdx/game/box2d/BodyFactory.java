package com.mygdx.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {

    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;
    private final float DEGTORAD = 0.0174533f;
    private static BodyFactory thisInstance;
    private World world;

    private BodyFactory(World world) {
        this.world = world;
    }

    public static BodyFactory getInstance(World world) {
        if (thisInstance == null) {
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    private static FixtureDef makeFixture(int material, Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch (material) {
            case STEEL:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case WOOD:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0.01f;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }
        return fixtureDef;
    }

    Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyDef.BodyType bodyType, boolean fixedRotation) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        // create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius / 2);
        boxBody.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();
        return boxBody;
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyDef.BodyType bodyType) {
        return makeCirclePolyBody(posx, posy, radius, material, bodyType, false);
    }

    Body makeBoxBody(float posx, float posy, float dx, float dy, int material, BodyDef.BodyType bodyType, boolean fixedRotation) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(dx, dy);
        boxBody.createFixture(makeFixture(material, boxShape));
        boxShape.dispose();
        return boxBody;
    }
    public Body makePolygonShapeBody(int verticesNbr, float posx, float posy, int material, BodyDef.BodyType bodyType) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        Body boxBody = world.createBody(boxBodyDef);

        PolygonShape polygon = new PolygonShape();
        Vector2[] vertices = new Vector2[verticesNbr];
        double radius = 2;
        for (int i = 0; i < verticesNbr; i++) {
            double angle = i * (2 * Math.PI) / verticesNbr;
            vertices[i] = new Vector2(
                    (float) (radius * Math.cos(angle) + 2 * radius),
                    (float) (radius * Math.sin(angle) + 2 * radius));
        }
        polygon.set(vertices);
        boxBody.createFixture(makeFixture(material, polygon));
        polygon.dispose();

        return boxBody;
    }

    public void makeConeSensor(Body body, float size) {

        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.isSensor = true; // will add in future

        PolygonShape polygon = new PolygonShape();

        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 145 * DEGTORAD); // convert degrees to radians
            vertices[i - 1] = new Vector2(radius * ((float) Math.cos(angle)), radius * ((float) Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }

    public void makeAllFixturesSensors(Body bod) {
        for (Fixture fix : bod.getFixtureList()) {
            fix.setSensor(true);
        }
    }
}