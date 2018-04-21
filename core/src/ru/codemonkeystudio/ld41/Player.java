package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    Body body;
    Texture texture;
    TextureRegion region;

    int num;
    float x, y;
    boolean jumpD = false;
    int health = 100;
    float timer = 0.5f;

    public Player(int num, World world, float x, float y) {
        this.num = num;

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((x + 24 / 2) / OLD.SCALE, (y + 64 / 2) / OLD.SCALE);
        this.x = (x + 24 / 2) / OLD.SCALE;
        this.y = (y + 64 / 2) / OLD.SCALE;

        body = world.createBody(bDef);

        shape.setAsBox((24 / 2) / OLD.SCALE, (64 / 2) / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("player" + num);

        texture = new Texture("player/player" + num + ".png");
        region = new TextureRegion(texture);
    }

    public void update(float delta) {
        if (Math.abs(getPos().x - x) > 10 / OLD.SCALE) {
            body.setLinearVelocity((getPos().x < x) ? 40 / OLD.SCALE : -40 / OLD.SCALE, getVel().y);
        }
        if (Math.abs(getPos().y - y) > 60 / OLD.SCALE) {
            if (getPos().y > y) {
                jumpDown();
            }
            else {
                jumpUp();
            }
        }

        int min = 0;
        for (Enemy i : GameScreen.enemies) {
            if (getPos().dst(i.getPos()) < getPos().dst(GameScreen.enemies.get(min).getPos())) {
                min = i.num;
            }
        }
        if (getPos().dst(GameScreen.enemies.get(min).getPos()) < 200 / OLD.SCALE) {
            timer -= delta;
            if (timer < 0) {
                GameScreen.createBullet(getPos(), getPos().cpy().sub(GameScreen.enemies.get(min).getPos()).setLength(400).rotate(180));
                timer = 0.5f;
            }
        } else {
            timer = 0.5f;
        }
    }

    public void draw(SpriteBatch batch) {
        if (getVel().x < 0)
            region.setRegion(0, 0, 24, 64);
        if (getVel().x == 0)
            region.setRegion(24, 0, 24, 64);
        if (getVel().x > 0)
            region.setRegion(48, 0, 24, 64);
        batch.draw(region, (getPos().x - 24 / 2 / OLD.SCALE) * OLD.SCALE, (getPos().y - 64 / 2 / OLD.SCALE) * OLD.SCALE);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public Vector2 getVel() {
        return body.getLinearVelocity();
    }

    public void jumpUp() {
        if (getVel().y == 0) {
            body.setLinearVelocity(getVel().x, 150 / OLD.SCALE);
        }
    }

    public void jumpDown() {
        if (getVel().y == 0) {
            jumpD = true;
        }
    }
}
