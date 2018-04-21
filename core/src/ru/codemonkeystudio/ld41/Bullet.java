package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet {
    Body body;

    public Bullet(World world, Vector2 pos, Vector2 vel) {
        BodyDef bDef = new BodyDef();
        CircleShape shape = new CircleShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set(pos);

        body = world.createBody(bDef);

        shape.setRadius(8 / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("bullet");

        body.setLinearVelocity(vel);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
