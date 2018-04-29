package com.plcheese.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class ActorManger {

    public static BaseActor createActor(Stage stage, String pathToTexture, float x, float y) {
        return createActor(stage, pathToTexture, x, y, true);
    }

    public static BaseActor createActor(Stage stage, String pathToTexture, float x, float y, boolean visible) {

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

    public static AnimatedActor createAnimetedActor(Stage stage, Animation animation, float x, float y) {

        AnimatedActor actor = new AnimatedActor();
        actor.setAnimimation(animation);
        actor.setPosition(x,y);
        actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
        stage.addActor(actor);

        return actor;

    }

    public static Animation mouseAnimation() {

        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            String fileName = "mouse_" + i + ".png";
            Texture tex = new Texture(Gdx.files.internal(fileName));
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames[i] = new TextureRegion(tex);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

    }

}
