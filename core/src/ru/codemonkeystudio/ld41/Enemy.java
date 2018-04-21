package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Enemy {
    Body body;
    Texture texture;
    TextureRegion region;

    boolean enabled = false;
    boolean jumpD = false;

    int num;

    float timer = 1f;

    public Enemy(World world, int num, float x, float y) {
        this.num = num;
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((x + 24 / 2) / OLD.SCALE, (y + 64 / 2) / OLD.SCALE);

        body = world.createBody(bDef);

        shape.setAsBox((24 / 2) / OLD.SCALE, (64 / 2) / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("enemy" + num);

        texture = new Texture("enemy.png");
        region = new TextureRegion(texture);
    }

    public void update(float delta) {
        int min = 0;
        for (Player i : GameScreen.players) {
            if (getPos().dst(i.getPos()) < getPos().dst(GameScreen.players.get(min).getPos())) {
                min = i.num;
            }
        }
        if (getPos().dst(GameScreen.players.get(min).getPos()) < 200 / OLD.SCALE) {
            enabled = true;
        }

        if (enabled) {
            if (Math.abs(getPos().y - GameScreen.players.get(min).getPos().y) > 50 / OLD.SCALE) {
                if (getPos().y < GameScreen.players.get(min).getPos().y) {
                    jumpUp();
                }
                else {
                    jumpDown();
                }
            }

            if (getPos().x < GameScreen.players.get(min).getPos().x) {
                if (GameScreen.players.get(min).getPos().x - getPos().x < 200 / OLD.SCALE) {
                    body.setLinearVelocity(-20 / OLD.SCALE, getVel().y);
                }
                else {
                    body.setLinearVelocity(20 / OLD.SCALE, getVel().y);
                }
            } else {
                if (- GameScreen.players.get(min).getPos().x + getPos().x < 200 / OLD.SCALE) {
                    body.setLinearVelocity(20 / OLD.SCALE, getVel().y);
                }
                else {
                    body.setLinearVelocity(-20 / OLD.SCALE, getVel().y);
                }
            }

            timer -= delta;
            if (timer < 0) {
                timer += 1f;
                GameScreen.createBullet(getPos(), getPos().cpy().sub(GameScreen.players.get(min).getPos()).setLength(400).rotate(180));
            }
        }
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

    public void draw(SpriteBatch batch) {
        if (!enabled)
            region.setRegion(24, 0, 24, 64);
        else if (getVel().x < 0)
            region.setRegion(0, 0, 24, 64);
        else
            region.setRegion(48, 0, 24, 64);
        batch.draw(region, (getPos().x - 24 / 2 / OLD.SCALE) * OLD.SCALE, (getPos().y - 64 / 2 / OLD.SCALE) * OLD.SCALE);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public Vector2 getVel() {
        return body.getLinearVelocity();
    }
}
