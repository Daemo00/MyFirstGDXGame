package com.mygdx.game.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.box2d.entity.components.CollisionComponent;

public class Box2DContactListener implements ContactListener {

    public Box2DContactListener() {
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

        if (fa.getBody().getUserData() instanceof Entity
                && fb.getBody().getUserData() instanceof Entity) {
            Entity entA = (Entity) fa.getBody().getUserData();
            Entity entB = (Entity) fb.getBody().getUserData();
            entityCollision(entA, entB);
            return;
        }
        System.out.println("Collision not managed");
    }

    private void entityCollision(Entity entA, Entity entB) {
        CollisionComponent colA = entA.getComponent(CollisionComponent.class);
        CollisionComponent colB = entB.getComponent(CollisionComponent.class);

        if (colA != null) colA.collisionEntity = entB;
        System.out.println("ColA: " + colA);
        if (colB != null) colB.collisionEntity = entA;
        System.out.println("ColB: " + colB);
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("Contact end");
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}