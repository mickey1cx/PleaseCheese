package com.plcheese.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class CheeseLevel implements Screen {

    private Game game;

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

    private float timeElapsed;
    private Label timeLabel;

    public CheeseLevel(Game game) {

        this.game = game;
        init();

    }

    private void init() {

        mainStage = new Stage();
        uiStage = new Stage();

        initActions();

        floor = ActorManger.createActor(mainStage,"tiles.jpg", 500, 500);
        cheese = ActorManger.createActor(mainStage,"cheese.png", MathUtils.random(100, 900), MathUtils.random(100, 900));
        mousey = ActorManger.createAnimetedActor(mainStage, ActorManger.mouseAnimation(), 20, 20);
        winText = ActorManger.createActor(uiStage,"you-win.png", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /2, false);

        win = false;

        timeElapsed = 0;

        BitmapFont font = new BitmapFont();
        String text = "Time 0";
        Label.LabelStyle style = new Label.LabelStyle(font, Color.NAVY);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(2);
        timeLabel.setPosition(4, Gdx.graphics.getHeight() - 24);

        uiStage.addActor(timeLabel);

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
                Actions.sequence(
                        Actions.sequence(
                                Actions.color(new Color(0.5f, 0.5f, 0.5f, 1), 1),
                                Actions.color(new Color(1, 1, 1, 1), 1)
                        )
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                })
        );

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        mousey.velocityY = 0;
        mousey.velocityX = 0;

        if (Gdx.input.isKeyPressed(Keys.LEFT)) mousey.velocityX -= velocity;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) mousey.velocityX += velocity;
        if (Gdx.input.isKeyPressed(Keys.UP)) mousey.velocityY += velocity;
        if (Gdx.input.isKeyPressed(Keys.DOWN)) mousey.velocityY -= velocity;

        if (Gdx.input.isKeyPressed(Keys.M))
            game.setScreen(new CheeseMenu(game));

        if (Gdx.input.isTouched()) {

            float tx = Gdx.input.getX();
            float ty = Gdx.input.getY();
            Vector2 v = mainStage.screenToStageCoordinates(new Vector2(tx, ty));

            float dx = v.x - (mousey.getX() + mousey.getOriginX());
            float dy = v.y - (mousey.getY() + mousey.getOriginY());
            float angle = MathUtils.atan2(dy, dx);

            mousey.velocityY += velocity * MathUtils.sin(angle);
            mousey.velocityX += velocity * MathUtils.cos(angle);

        }

        mainStage.act(delta);
        uiStage.act(delta);

        mousey.setX(MathUtils.clamp(mousey.getX(), 0, mapWidth - mousey.getWidth()));
        mousey.setY(MathUtils.clamp(mousey.getY(), 0, mapHeight - mousey.getHeight()));

        Rectangle cheeseRect = cheese.getBoundingRectangle();
        Rectangle mouseyRect = mousey.getBoundingRectangle();
        if (!win && cheeseRect.contains(mouseyRect)) {
            win = true;
            cheese.addAction(spinShrinkFadeOut);
            winText.addAction(fadeInColorCycleForever);
        }

        if(!win) {
            timeElapsed += delta;
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
