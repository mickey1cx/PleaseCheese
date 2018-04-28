package com.plcheese.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

public class PleaseCheese extends Game {

	final int mapWidth = 1000;
	final int mapHeight = 1000;

	final int viewWidth = 480;
	final int viewHeight = 320;

	public Stage mainStage;
	private Stage uiStage;

	private AnimatedActor mousey;
	private BaseActor cheese;
	private BaseActor floor;
	private BaseActor winText;
	private float velocity = 100.0f;

	private boolean win;
	private Action spinShrinkFadeOut;
	private Action fadeInColorCycleForever;

	private Animation mouseAnim;

	private float timeElapsed;
	private Label timeLabel;

	@Override
	public void create () {

		mainStage = new Stage();
		uiStage = new Stage();

		initActions();
		initAnimation();

		floor = createActor(mainStage,"tiles.jpg", 500, 500);
		cheese = createActor(mainStage,"cheese.png", MathUtils.random(100, 900), MathUtils.random(100, 900));
		mousey = createAnimetedActor(mouseAnim, 20, 20);
		winText = createActor(uiStage,"you-win.png", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /2, false);

		win = false;

		timeElapsed = 0;

		BitmapFont font = new BitmapFont();
		String text = "Time 0";
		LabelStyle style = new LabelStyle(font, Color.NAVY);
		timeLabel = new Label(text, style);
		timeLabel.setFontScale(2);
		timeLabel.setPosition(4, Gdx.graphics.getHeight() - 24);

		uiStage.addActor(timeLabel);

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

	private BaseActor createActor(Stage stage, String pathToTexture, float x, float y) {
		return createActor(stage, pathToTexture, x, y, true);
	}

	private BaseActor createActor(Stage stage, String pathToTexture, float x, float y, boolean visible) {

		BaseActor actor = new BaseActor();
		actor.setTexture(new Texture(
				Gdx.files.internal(pathToTexture)
		));
		actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
		actor.setPosition(x - actor.getOriginX(),y - actor.getOriginY());
		actor.setVisible(visible);
		stage.addActor(actor);

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
		uiStage.act(dt);

//		if (mousey.getX() < 0) mousey.setX(0);
//		else if (mousey.getX() > mapWidth - mousey.getWidth()) mousey.setX(mapWidth - mousey.getWidth());
		mousey.setX(MathUtils.clamp(mousey.getX(), 0, mapWidth - mousey.getWidth()));
//
//		if (mousey.getY() < 0) mousey.setY(0);
//		else if (mousey.getY() > mapHeight - mousey.getHeight()) mousey.setY(mapHeight - mousey.getHeight());
		mousey.setY(MathUtils.clamp(mousey.getY(), 0, mapHeight - mousey.getHeight()));



		Rectangle cheeseRect = cheese.getBoundingRectangle();
		Rectangle mouseyRect = mousey.getBoundingRectangle();
		if (!win && cheeseRect.contains(mouseyRect)) {
			//winText.setVisible(true);
			win = true;
			cheese.addAction(spinShrinkFadeOut);
			winText.addAction(fadeInColorCycleForever);
		}

		if(!win) {
			timeElapsed += dt;
			timeLabel.setText("Time: " + (int) timeElapsed);
		}



			Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Camera cam = mainStage.getCamera();
		cam.position.set(mousey.getX() + mousey.getOriginY(), mousey.getY() + mousey.getOriginY(), 0);
		cam.position.x = MathUtils.clamp(cam.position.x, viewWidth / 2, mapWidth - viewWidth / 2);
		cam.position.y = MathUtils.clamp(cam.position.y, viewHeight / 2, mapWidth - viewHeight / 2);
		cam.update();

		mainStage.draw();
		uiStage.draw();

	}
	
	@Override
	public void dispose () {

	}
}
