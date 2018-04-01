package com.plcheese.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class PleaseCheese extends Game {

	public Stage mainStage;
	private BaseActor mousey;
	private BaseActor cheese;
	private BaseActor floor;
	private BaseActor winText;

	private boolean win;
	private Action spinShrinkFadeOut;
	private Action fadeInColorCycleForever;

	@Override
	public void create () {

		mainStage = new Stage();

		floor = createActor("tiles.jpg", 0, 0);
		cheese = createActor("cheese.png", 400, 300);
		mousey = createActor("mouse.png", 20, 20);
		winText = createActor("you-win.png", 170, 60, false);

		win = false;

		createActions();

	}

	private void createActions() {

		spinShrinkFadeOut = Actions.parallel(
				Actions.alpha(1),
				Actions.rotateBy(360, 1),
				Actions.scaleTo(0, 0, 1),
				Actions.fadeOut(1)
		);

		fadeInColorCycleForever = Actions.sequence(
				Actions.alpha(0),
				Actions.show(),
				Actions.fadeIn(2),
				Actions.forever(
						Actions.sequence(
								Actions.color(new Color(1, 0, 0, 1), 1),
								Actions.color(new Color(1, 0, 1, 1), 1)
						)
				)
		);

	}

	private BaseActor createActor(String pathToTexture, float x, float y) {
		return createActor(pathToTexture, x, y, true);
	}

	private BaseActor createActor(String pathToTexture, float x, float y, boolean visible) {

		BaseActor actor = new BaseActor();
		actor.setTexture(new Texture(
				Gdx.files.internal(pathToTexture)
		));
		actor.setPosition(x,y);
		actor.setVisible(visible);
		actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
		mainStage.addActor(actor);

		return actor;

	}

	@Override
	public void render () {

		mousey.velocityY = 0;
		mousey.velocityX = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) mousey.velocityX -= 100;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) mousey.velocityX += 100;
		if (Gdx.input.isKeyPressed(Keys.UP)) mousey.velocityY += 100;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) mousey.velocityY -= 100;

		float dt = Gdx.graphics.getDeltaTime();
		mainStage.act(dt);

		Rectangle cheeseRect = cheese.getBoundingRectangle();
		Rectangle mouseyRect = mousey.getBoundingRectangle();
		if (!win && cheeseRect.contains(mouseyRect)) {
			//winText.setVisible(true);
			win = true;
			cheese.addAction(spinShrinkFadeOut);
			winText.addAction(fadeInColorCycleForever);
		}

		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mainStage.draw();

	}
	
	@Override
	public void dispose () {

	}
}
