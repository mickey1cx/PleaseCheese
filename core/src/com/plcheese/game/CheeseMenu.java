package com.plcheese.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class CheeseMenu extends BaseScreen {

    public CheeseMenu(Game game) {

        super(game);

    }

    protected void init() {

        BaseActor title = ActorManger.createActor(uiStage, "menu_logo.png",
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /2);

        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        String text = "S or touch - start, M - main menu";
        LabelStyle style = new LabelStyle(font, Color.FIREBRICK);
        Label instructions = new Label(text, style);
        instructions.setFontScale(2);
        instructions.setPosition(Gdx.graphics.getWidth() / 2 - instructions.getWidth() / 2, 25);
        instructions.setAlignment(Align.center, Align.center);

        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color(new Color(1,1,0,1), 0.5f),
                                Actions.delay(0.5f),
                                Actions.color(new Color(0.25f,0.25f,0,1), 0.5f)
                        )
                )
        );


        uiStage.addActor(instructions);

    }

    public boolean keyDown(int keycode) {

        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isTouched())
            game.setScreen(new CheeseLevel(game));

        return false;

    }

    @Override
    protected void update(float delta) {

    }


    @Override
    public void show() {

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
