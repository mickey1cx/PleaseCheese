package com.plcheese.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

public class PleaseCheese extends Game {

	public Stage mainStage;
	private AnimatedActor mousey;
	private BaseActor cheese;
	private BaseActor floor;
	private BaseActor winText;
	private float velocity = 100.0f;

	private boolean win;
	private Action spinShrinkFadeOut;
	private Action fadeInColorCycleForever;

	private Animation mouseAnim;

	@Override
	public void create () {

		mainStage = new Stage();

		initActions();
		initAnimation();

		floor = createActor("tiles.jpg", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /2);
		cheese = createActor("cheese.png", 400, 300);
		mousey = createAnimetedActor(mouseAnim, 20, 20);
		winText = createActor("you-win.png", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /2, false);

		win = false;


	}

	private void initAnimation() {

		TextureRegion[] frames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			String fileName = "mouse_" + i + ".png";
			Texture tex = new Texture(Gdx.files.internal(fileName));
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			frames[i] = new TextureRegion(tex);
		}
		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
		mouseAnim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

	}

	private void initActions() {

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
								Actions.color(new Color(0.5f, 0.5f, 0.5f, 1), 1),
								Actions.color(new Color(1, 1, 1, 1), 1)
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
		actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
		actor.setPosition(x - actor.getOriginX(),y - actor.getOriginY());
		actor.setVisible(visible);
		mainStage.addActor(actor);

		return actor;

	}

	private AnimatedActor createAnimetedActor(Animation animation, float x, float y) {

		AnimatedActor actor = new AnimatedActor();
		actor.setAnimimation(animation);
		actor.setPosition(x,y);
		actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
		mainStage.addActor(actor);

		return actor;

	}

	@Override
	public void render () {

		mousey.velocityY = 0;
		mousey.velocityX = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) mousey.velocityX -= velocity;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) mousey.velocityX += velocity;
		if (Gdx.input.isKeyPressed(Keys.UP)) mousey.velocityY += velocity;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) mousey.velocityY -= velocity;

		if (Gdx.input.isTouched()) {

			float dx = Gdx.input.getX() - (mousey.getX() + mousey.getOriginX());
			float dy = (Gdx.graphics.getHeight() - Gdx.input.getY()) - (mousey.getY() + mousey.getOriginY());
			float angle = MathUtils.atan2(dy, dx);


			mousey.velocityY += velocity * MathUtils.sin(angle);
			mousey.velocityX += velocity * MathUtils.cos(angle);

		}

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
