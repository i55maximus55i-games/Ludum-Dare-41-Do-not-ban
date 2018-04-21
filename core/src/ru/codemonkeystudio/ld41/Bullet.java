package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.physics.box2d.*;

public class Bullet {
    Body body;
    public Bullet(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((x + 24 / 2) / OLD.SCALE, (y + 64 / 2) / OLD.SCALE);

        body = world.createBody(bDef);

        shape.setAsBox((24 / 2) / OLD.SCALE, (64 / 2) / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("enemy");
    }
}
