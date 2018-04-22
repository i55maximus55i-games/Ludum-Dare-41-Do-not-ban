package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class GameScreen implements Screen {
    static TiledMap map;
    static World world;

    SpriteBatch batch;
    OrthogonalTiledMapRenderer mapRenderer;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    Viewport viewport;

    Stage stage;

    static ArrayList<Player> players;
    static ArrayList<Enemy> enemies;
    static ArrayList<Bullet> bullets;
    static Boss boss;

    float mx, my;
    int selectedPlayer = 0;

    Texture texture;
    Texture[] textures = {
            new Texture("screamers/amazon.png"),
            new Texture("screamers/digit.png"),
            new Texture("screamers/git.png"),
            new Texture("screamers/google.jpg"),
            new Texture("screamers/twitch.png"),
            new Texture("screamers/huy.jpg")
    };

    static int s = -1;

    Music m1 = Gdx.audio.newMusic(Gdx.files.internal("music/theme.mp3"));
    Music m2 = Gdx.audio.newMusic(Gdx.files.internal("music/boss-fight.mp3"));
    boolean a = true;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();

        map = new TmxMapLoader().load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        world = new World(new Vector2(0, -20), false);
        world.setContactListener(new ContactHandler());
        debugRenderer = new Box2DDebugRenderer();

        createWalls();
        createPlayers();
        createEnemies();

        mx = Gdx.input.getX();
        my = Gdx.input.getY();

        createStage();
        bullets = new ArrayList<Bullet>();
        texture = new Texture("bullet.png");

        boss = new Boss();

        m1.setLooping(true);
        m2.setLooping(true);
        m1.play();
    }

    @Override
    public void render(float delta) {
        int d = 0;
        for (Player i : players) {
            i.update(delta);
            if (i.health <= 0)
                d++;
        }
        if (d >= 4) {
            m1.stop();
            m2.stop();
            OLD.game.setScreen(new TgWinScreen());
        }
        if (boss.health <= 0) {
            m1.stop();
            m2.stop();
            OLD.game.setScreen(new RWinScreen());
        }
        for (Enemy i : enemies) {
            i.update(delta);
        }
        boss.update(delta);
        world.step(delta, 10, 10);

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            camera.position.x -= Gdx.input.getX() - mx;
            camera.position.y += Gdx.input.getY() - my;
        }
        mx = Gdx.input.getX();
        my = Gdx.input.getY();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);

        mapRenderer.render();
        batch.begin();
        for (Player i : players) {
            i.draw(batch);
        }
        for (Enemy i : enemies) {
            i.draw(batch);
        }
        for (Bullet i : bullets) {
            batch.draw(texture, (i.getPos().x - 16 / 2 / OLD.SCALE) * OLD.SCALE, (i.getPos().y - 16 / 2 / OLD.SCALE) * OLD.SCALE);
        }
        if (s >= 0) {
            batch.draw(textures[s], camera.position.x - textures[s].getWidth() / 2, camera.position.y - textures[s].getHeight() / 2);
            s = -1;
        }
        boss.draw(batch);
        batch.end();
//        debugRenderer.render(world, camera.combined);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            players.get(selectedPlayer).x = (Gdx.input.getX() + camera.position.x - Gdx.graphics.getWidth() / 2) / OLD.SCALE;
            players.get(selectedPlayer).y = (Gdx.graphics.getHeight() / 2 - Gdx.input.getY() + camera.position.y) / OLD.SCALE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            selectedPlayer = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            selectedPlayer = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            selectedPlayer = 2;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
            selectedPlayer = 3;

        stage.draw();
        stage.dispose();
        createStage();

        if (boss.health < 20 && a) {
            m1.stop();
            m2.play();
            a = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        m1.dispose();
        m2.dispose();
    }

    private void createWalls() {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        for (RectangleMapObject i : map.getLayers().get("walls").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / OLD.SCALE, (rect.getY() + rect.getHeight() / 2) / OLD.SCALE);

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / OLD.SCALE, rect.getHeight() / 2 / OLD.SCALE);
            fDef.shape = shape;
            body.createFixture(fDef);
            body.setUserData("wall");
        }
    }

    private void createPlayers() {
        players = new ArrayList<Player>();
        int a = 0;
        for (RectangleMapObject i : map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            players.add(new Player(a++, world, rect.getX(), rect.getY()));
        }
    }

    private void createEnemies() {
        enemies = new ArrayList<Enemy>();
        int a = 0;
        for (RectangleMapObject i : map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            enemies.add(new Enemy(world, a++, rect.getX(), rect.getY()));
        }
    }

    private void createStage() {
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.left();
        for (Player i : players) {
            if (i.num == selectedPlayer) {
                Label label = new Label(">Player " + (i.num + 1) + "       " + i.health + "/100", Styles.labelStyle);
                if (i.health <= 0)
                    label = new Label(">Player " + (i.num + 1) + "       " + "DEAD", Styles.labelStyle);
                table.add(label);
                table.row();
            } else {
                Label label = new Label(" Player " + (i.num + 1) + "       " + i.health + "/100", Styles.labelStyle);
                if (i.health <= 0)
                    label = new Label(" Player " + (i.num + 1) + "       " + "DEAD", Styles.labelStyle);
                table.add(label);
                table.row();
            }
        }
        stage.addActor(table);

        table = new Table();
        table.setFillParent(true);
        table.top();
        table.right();
        int n = 0;
        for (Enemy i : enemies) {
            if (i.health <= 0) {
                n++;
            }
        }
        Label label = new Label("Диванных войск уничтожено: " + n, Styles.labelStyle);
        table.add(label);
        stage.addActor(table);
    }

    static void createBullet(Vector2 pos, Vector2 vel, boolean rkn) {
        bullets.add(new Bullet(world, pos, vel, rkn));
    }
}
