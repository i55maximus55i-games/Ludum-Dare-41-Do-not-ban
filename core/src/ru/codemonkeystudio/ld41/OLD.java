package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class OLD extends Game {
	public static OLD game;

	private ShapeRenderer shapeRenderer;

	private float blend;
	private float blendDelta;
	private Color blendColor;

	private Color color;
	private Screen screen;

	public static final float SCALE = 4f;

	@Override
	public void create() {
		game = this;
		Styles.init();
		shapeRenderer = new ShapeRenderer();
		color = Color.WHITE;
		setScreen(new SplashScreen(), Color.WHITE, 0.015f);
		blend = -0.3f;
	}

	public void setScreen(Screen screen, Color color, float delta) {
		this.screen = screen;
		flash(color, delta);
	}

	public void flash(Color color, float delta) {
		blend = -1.1f;
		blendDelta = delta;
		blendColor = color;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(blendColor.r, blendColor.g, blendColor.b, - blend * blend + 1.1f);
		blend += blendDelta;
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();

		if (screen != null && blend >= 0f) {
			setScreen(screen);
			screen = null;
			color = blendColor;
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		shapeRenderer = new ShapeRenderer();
	}
}
