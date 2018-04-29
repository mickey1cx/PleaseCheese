package com.plcheese.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class AnimatedActor extends BaseActor {

    private Animation<TextureRegion> anim;
    public float elapsedTime;


    public AnimatedActor() {

        super();
        elapsedTime = 0;

    }

    public void setAnimimation(Animation<TextureRegion> a) {

        setTexture(a.getKeyFrame(0).getTexture());
        anim = a;
    }

    @Override
    public void act(float dt) {

        super.act(dt);

        elapsedTime += dt;

        if(velocityX !=0 || velocityY !=0) {
            setRotation(MathUtils.atan2(velocityY, velocityX) * MathUtils.radiansToDegrees);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        region.setRegion(anim.getKeyFrame(elapsedTime));
        super.draw(batch, parentAlpha);

    }
}
