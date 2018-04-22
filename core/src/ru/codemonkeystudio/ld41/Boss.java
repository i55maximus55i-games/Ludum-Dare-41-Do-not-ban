package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.ArrayList;

public class Boss {
    Texture texture;

    ArrayList<Vector2> way;
    Vector2 pos;
    int i = 1;

    Body body;
    int health = 20;

    float timer = 4f;

    public Boss () {
        way = new ArrayList<Vector2>();
        for (RectangleMapObject i : GameScreen.map.getLayers().get("boss").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle r = i.getRectangle();
            way.add(new Vector2(r.getX(), r.getY()));
        }
        pos = way.get(0).cpy();


        BodyDef bDef = new BodyDef();
        CircleShape shape = new CircleShape();
        FixtureDef fDef = new FixtureDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(pos);

        body = GameScreen.world.createBody(bDef);

        shape.setRadius(64 / OLD.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("boss");


        texture = new Texture("boss.png");
    }

    public void update(float delta) {
        if (pos.dst(way.get(i)) < 50) {
            i++;
            if (i >= way.size())
                i = 0;
        }
        pos.add(way.get(i).cpy().sub(pos).setLength(30 / OLD.SCALE));
        body.setTransform(pos.cpy().scl(1 / OLD.SCALE), 0);

        if (health != 20) {
            timer -= delta;
            if (timer < 0) {
                timer = 4f;
                for (Player i : GameScreen.players)
                    i.health -= 5;
                OLD.game.flash(Color.RED, 0.15f);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2);
    }
}
