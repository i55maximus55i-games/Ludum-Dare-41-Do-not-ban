package ru.codemonkeystudio.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Styles {
    public static BitmapFont font16;
    public static BitmapFont font24;
    public static BitmapFont font32;

    public static Label.LabelStyle labelStyle;

    public static void init() {
        font16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font16;
    }
}
