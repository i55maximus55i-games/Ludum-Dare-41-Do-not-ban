package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getBody().getUserData().toString().contains("player")) {
            if (GameScreen.players.get(contact.getFixtureB().getBody().getUserData().toString().charAt(6) - '0').jumpD) {
                GameScreen.players.get(contact.getFixtureB().getBody().getUserData().toString().charAt(6) - '0').jumpD = false;
            }
        }
        if (contact.getFixtureB().getBody().getUserData().toString().contains("enemy")) {
            if (GameScreen.players.get(Integer.parseInt(contact.getFixtureB().getBody().getUserData().toString().substring(5))).jumpD) {
                GameScreen.players.get(Integer.parseInt(contact.getFixtureB().getBody().getUserData().toString().substring(5))).jumpD = false;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (contact.getFixtureB().getBody().getUserData().toString().contains("player")) {
            if (GameScreen.players.get(contact.getFixtureB().getBody().getUserData().toString().charAt(6) - '0').jumpD) {
                contact.setEnabled(false);
            }
        }
        if (contact.getFixtureB().getBody().getUserData().toString().contains("enemy")) {
            if (GameScreen.enemies.get(Integer.parseInt(contact.getFixtureB().getBody().getUserData().toString().substring(5))).jumpD) {
                contact.setEnabled(false);
            }
        }

        if (contact.getFixtureB().getBody().getLinearVelocity().y > 0) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
