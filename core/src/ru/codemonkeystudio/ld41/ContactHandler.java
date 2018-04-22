package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Random;

public class ContactHandler implements ContactListener {
    int p = 0;

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData().toString().contains("bullet") || contact.getFixtureB().getBody().getUserData().toString().contains("bullet")) {
            if (contact.getFixtureA().getBody().getUserData().toString().contains("player") && contact.getFixtureB().getBody().getUserData().toString().charAt(6) == 'd') {
                GameScreen.players.get(contact.getFixtureA().getBody().getUserData().toString().charAt(6) - '0').health -= 1;
            }
            if (contact.getFixtureA().getBody().getUserData().toString().contains("enemy") && contact.getFixtureB().getBody().getUserData().toString().charAt(6) == 'r') {
                GameScreen.enemies.get(Integer.parseInt(contact.getFixtureA().getBody().getUserData().toString().substring(5))).health -= 25;
                p++;
                if (p > 10) {
                    GameScreen.s = 5;
                }
                else {
                    GameScreen.s = new Random().nextInt(5);
                }
                if (p > 15)
                    p = 0;
            }

            if (contact.getFixtureA().getBody().getUserData().toString().contains("boss") || contact.getFixtureB().getBody().getUserData().toString().contains("boss")) {
                p++;
                if (p > 10) {
                    GameScreen.s = 5;
                }
                else {
                    GameScreen.s = new Random().nextInt(5);
                }
                if (p > 15)
                    p = 0;

                GameScreen.boss.health--;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getBody().getUserData().toString().contains("player")) {
            if (GameScreen.players.get(contact.getFixtureB().getBody().getUserData().toString().charAt(6) - '0').jumpD) {
                GameScreen.players.get(contact.getFixtureB().getBody().getUserData().toString().charAt(6) - '0').jumpD = false;
            }
        }
        if (contact.getFixtureB().getBody().getUserData().toString().contains("enemy")) {
            if (GameScreen.enemies.get(Integer.parseInt(contact.getFixtureB().getBody().getUserData().toString().substring(5))).jumpD) {
                GameScreen.enemies.get(Integer.parseInt(contact.getFixtureB().getBody().getUserData().toString().substring(5))).jumpD = false;
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

        if (contact.getFixtureA().getBody().getUserData().toString().contains("bullet") || contact.getFixtureB().getBody().getUserData().toString().contains("bullet")) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
