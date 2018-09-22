package com.mygdx.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import static com.mygdx.game.box2d.B2DModel.BOING_SOUND;

public class Box2DContactListener implements ContactListener {

    private B2DModel parent;

    Box2DContactListener(B2DModel parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = true;
            return;
        } else if (fb.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = true;
            return;
        }
        if (fa.getBody().getType() == BodyDef.BodyType.StaticBody)
            this.shootUpInAir(fa, fb);
        else if (fb.getBody().getType() == BodyDef.BodyType.StaticBody)
            this.shootUpInAir(fb, fa);
    }

    private void shootUpInAir(Fixture staticFixture, Fixture otherFixture) {
        System.out.println("Adding Force");
        otherFixture.getBody().applyForceToCenter(new Vector2(-100000, -100000), true);
        parent.playSound(BOING_SOUND);
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = false;
        } else if (fb.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}