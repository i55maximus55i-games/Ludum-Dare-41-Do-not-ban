package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
    SpriteBatch batch;
    Texture texture;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
