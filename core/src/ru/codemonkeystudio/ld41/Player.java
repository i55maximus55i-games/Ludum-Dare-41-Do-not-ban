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
        bDef.position.set((x + 30 / 2) / OLD.SCALE, (y + 44 / 2) / OLD.SCALE);
        this.x = (x + 30 / 2) / OLD.SCALE;
        this.y = (y + 44 / 2) / OLD.SCALE;

        body = world.createBody(bDef);

        shape.setAsBox((30 / 2) / OLD.SCALE, (44 / 2) / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("player" + num);

        texture = new Texture("player/player" + num + num + ".png");
        region = new TextureRegion(texture);
    }

    public void update(float delta) {
        if (health > 0) {
            if (Math.abs(getPos().x - x) > 10 / OLD.SCALE) {
                body.setLinearVelocity((getPos().x < x) ? 120 / OLD.SCALE : -120 / OLD.SCALE, getVel().y);
            }
            if (Math.abs(getPos().y - y) > 60 / OLD.SCALE) {
                if (getPos().y > y) {
                    jumpDown();
                } else {
                    jumpUp();
                }
            }

            int min = 0;
            for (Enemy i : GameScreen.enemies) {
                if (getPos().dst(i.getPos()) < getPos().dst(GameScreen.enemies.get(min).getPos()) && i.health > 0) {
                    min = i.num;
                }
            }
            if (getPos().dst(GameScreen.boss.pos.cpy().scl(1 / OLD.SCALE)) < 300 / OLD.SCALE) {
                timer -= delta;
                if (timer < 0) {
                    GameScreen.createBullet(getPos(), getPos().cpy().sub(GameScreen.boss.pos.cpy().scl(1 / OLD.SCALE)).setLength(400).rotate(180), true);
                    timer = 0.5f;
                }
            }
            else if (getPos().dst(GameScreen.enemies.get(min).getPos()) < 200 / OLD.SCALE && GameScreen.enemies.get(min).health > 0) {
                timer -= delta;
                if (timer < 0) {
                    GameScreen.createBullet(getPos(), getPos().cpy().sub(GameScreen.enemies.get(min).getPos()).setLength(400).rotate(180), true);
                    timer = 0.5f;
                }
            } else {
                timer = 0.5f;
            }
        }
        if (getPos().y < 0)
            health = 0;
    }

    public void draw(SpriteBatch batch) {
        if (getVel().x < 0)
            region.setRegion(0, 0, 30, 44);
        if (getVel().x == 0)
            region.setRegion(30, 0, 30, 44);
        if (getVel().x > 0)
            region.setRegion(60, 0, 30, 44);
        batch.draw(region, (getPos().x - 30 / 2 / OLD.SCALE) * OLD.SCALE, (getPos().y - 44 / 2 / OLD.SCALE) * OLD.SCALE);
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
