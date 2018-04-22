package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture texture;
    private Viewport viewport;

    private int i;

    private Color[] colors = {
            new Color(1f, 1f, 1f, 0f),
            new Color(0.9450980392156863f, 0.9450980392156863f, 0.9450980392156863f, 0f),
            new Color(1, 1, 1, 0f)
    };
    private String[] textures = {
            "libgdx.jpg",
            "cms.bmp",
            "logo.bmp"
    };

    private float timerSplash;
    private boolean timerSplashE;
    private float timerSwitch;

    private boolean g;

    @Override
    public void show() {
        viewport = new ScreenViewport();
        batch = new SpriteBatch();

        i = 0;
        timerSplash = 0;
        timerSplashE = false;
        timerSwitch = 0;
        texture = new Texture("splashes/" + textures[i]);

        g = true;

        OLD.game.menu.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(colors[i].r, colors[i].g, colors[i].b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float x = Math.max((float)texture.getWidth() / (float)Gdx.graphics.getWidth(), (float)texture.getHeight() / (float)Gdx.graphics.getHeight());
        x /= 0.9f;

        batch.begin();
        batch.draw(texture, ((float) - texture.getWidth()) / x / 2, ((float) -texture.getHeight()) / x / 2, texture.getWidth() / x, texture.getHeight() / x);
        batch.end();
        if (g) {
            timerSplash += 0.015f;
            timerSwitch += delta;
            if (timerSwitch >= 1.5f) {
                timerSplash = 0;
                timerSplashE = true;
                timerSwitch = -1.5f;
                if (i < colors.length - 1)
                    OLD.game.flash(colors[i + 1], 0.015f);
            }
            if (timerSplash >= 1 && timerSplashE) {
                i++;
                timerSplashE = false;
                if (i >= textures.length) {
                    i--;
                } else {
                    texture = new Texture("splashes/" + textures[i]);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            OLD.game.setScreen(new GameScreen(), Color.BLACK, 0.1f);
            g = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        OLD.game.menu.stop();
    }

    @Override
    public void dispose() {

    }
}
